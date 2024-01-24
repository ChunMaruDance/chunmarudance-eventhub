package com.chunmaru.eventhub.screens.home_screen


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.EventTypeSelected
import com.chunmaru.eventhub.domain.EventRepository
import com.chunmaru.eventhub.domain.EventTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventTypeRepository: EventTypeRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private var _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Initial)
    val state = _state.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        get7EventsByCategories()
    }


    private fun get7EventsByCategories() {
        _state.value = HomeScreenState.PendingData
        viewModelScope.launch(Dispatchers.IO) {
            eventTypeRepository.getAllType().collect { eventTypes ->
                eventRepository.getFirst7ByCategories(eventTypes).collect { eventsByCategories ->
                    val eventTypeSelectedList = eventsByCategories.map { eventsCategory ->
                        EventTypeSelected(
                            selected = false,
                            eventsCategories = eventsCategory
                        )
                    }
                    Log.d("MyTag", "get7EventsByCategories: $eventTypeSelectedList ")
                    _state.value = HomeScreenState.ShowData(eventTypeSelectedList)
                }


            }

        }
    }

    fun clickOnCategories(selectedCategories: String) {
        if (_state.value is HomeScreenState.ShowData) {
            val showData = _state.value as HomeScreenState.ShowData
            val updatedEvents = showData.events.map { eventType ->
                if (eventType.eventsCategories.category == selectedCategories) {
                    eventType.copy(selected = !eventType.selected)
                } else {
                    eventType
                }
            }
            _state.value = HomeScreenState.ShowData(events = updatedEvents)
        }

    }

    fun loadProfile() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            eventTypeRepository.getAllType().collect { eventTypes ->
                eventRepository.getFirst7ByCategories(eventTypes).collect { eventsByCategories ->
                    val eventTypeSelectedList = eventsByCategories.map { eventsCategory ->
                        EventTypeSelected(
                            selected = false,
                            eventsCategories = eventsCategory
                        )
                    }
                    Log.d("MyTag", "get7EventsByCategories: $eventTypeSelectedList ")
                    _state.value = HomeScreenState.ShowData(eventTypeSelectedList)
                    _isLoading.value = false
                }

            }
        }
    }

}



