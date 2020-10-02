package com.ilnur.cardsnew.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilnur.cardsnew.database.Category
import com.ilnur.cardsnew.repository.TopicsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class TopicsViewModel @ViewModelInject constructor(private val repos: TopicsRepository): ViewModel() {

    val _topics = MutableLiveData<List<Category>>()
    val topics: LiveData<List<Category>> get() = _topics

    fun getTopics(predmet: String, parent_id: Int = 0) {
        CoroutineScope(Dispatchers.IO).launch{
            _topics.postValue(repos.getTopics(predmet, parent_id))
        }
    }
}