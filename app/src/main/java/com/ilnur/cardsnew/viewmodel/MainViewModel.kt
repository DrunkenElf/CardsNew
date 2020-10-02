package com.ilnur.cardsnew.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilnur.cardsnew.database.Subject
import com.ilnur.cardsnew.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MainViewModel @ViewModelInject constructor(private val repository: MainRepository) : ViewModel(){

    val _subjects = MutableLiveData<List<Subject>>()
    val subjects: LiveData<List<Subject>> get() = _subjects //список предметов

    init {
       CoroutineScope(Dispatchers.IO).launch{ _subjects.postValue(repository.getSubjects())}
    }

    val _currentSubj = MutableLiveData<Subject>()
    val currentSubj: LiveData<Subject> get() = _currentSubj

    fun selectSubject(subject: Subject){
        _currentSubj.postValue(subject)
    }

    fun updateSubjects(href: String){
        val updated =  _subjects.value!!
        updated.filter { it.href == href }.forEach { it.isAdded = true }
        Log.d("updated List", updated.toString())
        _subjects.postValue(updated)
    }



    fun subjs(): List<Subject> = arrayOf(
        Subject("Русский язык","rus"),
        Subject("Физика","phys"),
        Subject("Математика","math"),
        Subject("Английский язык","en"),
        Subject("История","hist")
    ).toList()

}

/*
data class Subject(
    val title: String,
    val href: String
)
data class AppState(
    var currSubject: Subject?,
    var currFragment
)*/
