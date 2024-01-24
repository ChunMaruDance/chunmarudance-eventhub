package com.chunmaru.hilt

import android.content.Context
import com.chunmaru.eventhub.data.repositories.AdminsRepositoryImpl
import com.chunmaru.eventhub.data.repositories.AuthorRepositoryImpl
import com.chunmaru.eventhub.data.repositories.DeletedEventsMessageRepositoryImpl
import com.chunmaru.eventhub.data.repositories.EventRepositoryImpl
import com.chunmaru.eventhub.data.repositories.EventTypeRepositoryImpl
import com.chunmaru.eventhub.data.repositories.ReviewsRepositoryImpl
import com.chunmaru.eventhub.data.repositories.StorageRepositoryImpl
import com.chunmaru.eventhub.domain.AdminsRepository
import com.chunmaru.eventhub.domain.AuthorRepository
import com.chunmaru.eventhub.domain.DeletedEventsMessageRepository
import com.chunmaru.eventhub.domain.EventRepository
import com.chunmaru.eventhub.domain.EventTypeRepository
import com.chunmaru.eventhub.domain.ReviewsRepository
import com.chunmaru.eventhub.domain.StorageRepository
import com.chunmaru.eventhub.screens.signIn_screen.CheckSignInFireBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context =
        context.applicationContext


    @Provides
    @Singleton
    fun provideEventTypeRepositoryImpl(): EventTypeRepository =
        EventTypeRepositoryImpl()


    @Provides
    @Singleton
    fun provideEventRepositoryImpl(): EventRepository =
        EventRepositoryImpl()


    @Provides
    @Singleton
    fun provideAuthorRepository(): AuthorRepository =
        AuthorRepositoryImpl()

    @Provides
    @Singleton
    fun provideStorageRepository(context: Context): StorageRepository =
        StorageRepositoryImpl(context)


    @Provides
    @Singleton
    fun provideCheckSignInFireBase(context: Context): CheckSignInFireBase =
        CheckSignInFireBase(context)

    @Provides
    @Singleton
    fun provideReviewsRepository(): ReviewsRepository =
        ReviewsRepositoryImpl()

    @Provides
    @Singleton
    fun provideAdminsRepository(): AdminsRepository =
        AdminsRepositoryImpl()

    @Provides
    @Singleton
    fun provideDeletedEventsMessageRepository(): DeletedEventsMessageRepository =
        DeletedEventsMessageRepositoryImpl()

}