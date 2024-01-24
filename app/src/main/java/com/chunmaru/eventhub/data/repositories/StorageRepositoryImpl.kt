package com.chunmaru.eventhub.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.net.toUri
import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.author.FireBaseAuthor
import com.chunmaru.eventhub.data.model.ImageData
import com.chunmaru.eventhub.data.model.ImageResult
import com.chunmaru.eventhub.domain.repositories.StorageRepository
import com.chunmaru.eventhub.domain.exceptions.ImageSizeExceededException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StorageRepositoryImpl(
    private val context: Context
) : StorageRepository {

    override suspend fun setAvatar(currentAuthor: Author, uri: String): ImageResult {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images")

        return try {

            val bitmap: Bitmap? = try {
                context.contentResolver.openInputStream(uri.toUri())?.use { input ->
                    BitmapFactory.decodeStream(input)
                }
            } catch (e: IOException) {
                null
            }

            if (bitmap != null && bitmap.byteCount > 10 * 1024 * 1024) {
                Log.d("MyTag", "setAvatar: more 10 ")
                throw ImageSizeExceededException.DEFAULT
            } else {
                val fileRef = storageRef.child(currentAuthor.avatar.path)
                if (currentAuthor.avatar.path != FireBaseAuthor.defaultPath) {
                    fileRef.delete().await()
                }

            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "image_$timestamp${currentAuthor.id}.jpg"
            val imageRef = storageRef.child(fileName)

            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = imageRef.putBytes(data)
            val downloadUrl = uploadTask.await().storage.downloadUrl.await().toString()

            val imageData = ImageData(path = fileName, uri = downloadUrl)
            ImageResult.Success(imageData)
        } catch (e: Exception) {
            ImageResult.Failure(e)
        }
    }

    override suspend fun saveEventImage(currentAuthorId: String, event: Event): ImageResult {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images")
        val uri = event.imgUri.uri
        val path = event.imgUri.path

        return try {
            val bitmap: Bitmap? = try {
                context.contentResolver.openInputStream(uri.toUri())?.use { input ->
                    BitmapFactory.decodeStream(input)
                }
            } catch (e: IOException) {
                null
            }
            Log.d("MyTag", "saveEventImage: $path ")
            if (path != FireBaseAuthor.defaultPath && path.isNotEmpty()) {
                val previousImageRef = storageRef.child(path)
                previousImageRef.delete().await()
                Log.d("MyTag", "saveEventImage 2: $path ")
            }

            if (bitmap != null && bitmap.byteCount > 15 * 1024 * 1024) {
                throw ImageSizeExceededException.DEFAULT
            } else {
                val timestamp =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "event_image_$timestamp${currentAuthorId}.jpg"
                val imageRef = storageRef.child(fileName)

                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                val uploadTask = imageRef.putBytes(data)
                val downloadUrl = uploadTask.await().storage.downloadUrl.await().toString()

                val imageData = ImageData(path = fileName, uri = downloadUrl)
                Log.d("MyTag", "saveEventImage 3: $path ")
                ImageResult.Success(imageData)
            }
        } catch (e: Exception) {
            Log.d("MyTag", "saveEventImage: $e ")
            ImageResult.Failure(e)
        }
    }

}