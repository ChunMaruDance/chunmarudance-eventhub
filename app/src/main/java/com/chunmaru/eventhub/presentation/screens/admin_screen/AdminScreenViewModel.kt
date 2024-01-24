package com.chunmaru.eventhub.presentation.screens.admin_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.event.DeletedEventInfo
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.event.EventType
import com.chunmaru.eventhub.domain.repositories.AuthorRepository
import com.chunmaru.eventhub.domain.repositories.DeletedEventsMessageRepository
import com.chunmaru.eventhub.domain.repositories.EventRepository
import com.chunmaru.eventhub.domain.repositories.EventTypeRepository
import com.chunmaru.eventhub.domain.repositories.ReviewsRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminScreenViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val deletedEventsMessageRepository: DeletedEventsMessageRepository,
    private val authorRepository: AuthorRepository,
    private val reviewsRepository: ReviewsRepository
) : ViewModel() {

    private var _state: MutableStateFlow<AdminScreenState> =
        MutableStateFlow(AdminScreenState.Initial)
    val state = _state.asStateFlow()

    private var _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    init {
        load()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("MyTag", ":logd12 ${reviewsRepository.getEventCountsInReviews()} ")
        }

    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = AdminScreenState.Pending
            eventTypeRepository.getAllType().collect { types ->
                _state.value = AdminScreenState.ShowData(
                    events = listOf(),
                    types = types.map {
                        EventType(type = it, false)
                    },
                    showStatistic = ShowStatisticsCategory.Initial,
                    showActiveAuthors = ShowActiveAuthors.Initial,
                    showReviewsStatistics = ShowReviewsStatistics.Initial
                )
            }

        }
    }


    fun showStatistic() {
        val currentState = _state.value as? AdminScreenState.ShowData ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val categories = currentState.types.map { it.type }

            val statistics = eventRepository.getEventCountsInCategories(categories)

            val sortedStatistics = statistics.sortedByDescending { it.eventCount }

            val top5Popular = sortedStatistics.take(5)
            val top5Unpopular = sortedStatistics.takeLast(5)

            val showStatistics = ShowStatisticsCategory.ShowStatistics(
                popularCategory = top5Popular,
                unpopularCategory = top5Unpopular
            )
            _state.value = currentState.copy(showStatistic = showStatistics)

        }

    }

    fun showActiveAuthors() {
        val currentState = _state.value as? AdminScreenState.ShowData ?: return
        viewModelScope.launch(Dispatchers.IO) {

            val authors = authorRepository.getAuthorsEventCounts()
            val topAuthors = authors.sortedByDescending { it.eventCount }.take(5)
            val showActiveAuthors = ShowActiveAuthors.ShowAuthors(topAuthors)

            _state.value = currentState.copy(showActiveAuthors = showActiveAuthors)

        }
    }

    fun showReviewsStatistic() {
        val currentState = _state.value as? AdminScreenState.ShowData ?: return
        viewModelScope.launch(Dispatchers.IO) {

            val reviewsEvent = reviewsRepository.getEventCountsInReviews()
            val topEvents = reviewsEvent.sortedByDescending { it.reviewCount }.take(5)
            val showReviews = ShowReviewsStatistics.ShowReviews(topEvents)

            _state.value = currentState.copy(showReviewsStatistics = showReviews)

        }
    }


    fun deleteType(
        type: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (eventTypeRepository.deleteType(type)) onSuccess()
            else onError()
        }
    }


    fun search(query: String) {
        val localeState = _state.value as? AdminScreenState.ShowData ?: return
        _searchText.value = query
        if (query.length > 3)
            performSearch(query, localeState)
        else if (query.isEmpty()) {
            _state.value = AdminScreenState.ShowData(
                listOf(),
                localeState.types,
                localeState.showStatistic,
                localeState.showActiveAuthors,
                localeState.showReviewsStatistics
            )
        }
    }

    private fun performSearch(query: String, localeState: AdminScreenState.ShowData) {
        viewModelScope.launch(Dispatchers.IO) {
            val events = eventRepository.searchEvent(query)
            Log.d("MyTag", "performSearch events: $events ")
            _state.value = localeState.copy(events = events)

        }
    }

    fun setCategory(type: String) {
        val currentState = _state.value
        if (currentState is AdminScreenState.ShowData) {
            val updatedTypes = currentState.types.map {
                if (it.type == type) it.copy(selected = !it.selected)
                else it
            }
            _state.value = currentState.copy(types = updatedTypes)
        }
    }

    fun deleteEvent(event: Event, reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (authorRepository.deleteEvent(event)) {

                if (eventRepository.deleteEvent(event))
                    Log.d("MyTag", "no deleteEvent: deleteEvent ")

                if (reviewsRepository.deleteAllReviews(event.id))
                    Log.d("MyTag", "no deleteEvent: reviewsRepository ")

                if (authorRepository.addToDeletedId(event.id, event.authorId))
                    Log.d("MyTag", "no deleteEvent: authorRepositoryAddToDeletedId ")


                if (deletedEventsMessageRepository.addDeletedEvent(
                        DeletedEventInfo(
                            eventId = event.id,
                            authorId = event.authorId,
                            eventName = event.name,
                            reason = reason,
                        )
                    )
                ) {
                    val currentShowDataState = _state.value as? AdminScreenState.ShowData
                    val updatedEvents = currentShowDataState?.events?.toMutableList()?.apply {
                        remove(event)
                    } ?: listOf()
                    _state.value = currentShowDataState?.copy(events = updatedEvents)!!
                    Log.d("MyTag", "deleteEvent: eventDelete ")
                }

            } else {
                Log.d("MyTag", "no deleteEvent: eventDelete ")
            }

        }

    }

    fun deleteTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _state.value
            if (currentState is AdminScreenState.ShowData) {
                val typesToDelete = currentState.types.filter { it.selected }.map { it.type }
                for (type in typesToDelete) {
                    if (eventTypeRepository.deleteType(type)) {
                        Log.d("MyTag", "deleteTypes: true ")
                        if (eventRepository.deleteCategory(type)) {
                            Log.d("MyTag", "deleteTypes: complete ")
                        }

                    } else {
                        Log.d("MyTag", "deleteTypes: false ")
                    }
                }
                val updatedTypes = currentState.types.filterNot { it.selected }
                _state.value = currentState.copy(types = updatedTypes)
            }
        }
    }

    fun cancelSelectCategory() {
        val currentState = _state.value
        if (currentState is AdminScreenState.ShowData) {
            val updatedTypes = currentState.types.map {
                it.copy(selected = false)
            }
            _state.value = currentState.copy(types = updatedTypes)
        }
    }

    fun createType(type: String) {
        val localeState = _state.value as? AdminScreenState.ShowData ?: return
        viewModelScope.launch(Dispatchers.IO) {
            if (eventTypeRepository.addType(type)) {
                Log.d("MyTag", "createType: true ")
                val types = localeState.types.toMutableList()
                types.add(EventType(type, false))
                _state.value = localeState.copy(types = types.toList())
            } else Log.d("MyTag", "createType: false ")
        }
    }

}