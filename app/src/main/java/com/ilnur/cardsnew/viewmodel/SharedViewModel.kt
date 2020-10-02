package com.ilnur.cardsnew.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilnur.cardsnew.database.Subject

class SharedViewModel: ViewModel() {
    val currentSubj = MutableLiveData<Subject>()

    fun selectSubject(subject: Subject){
        currentSubj.postValue(subject)
    }

    val subjects: List<Subject> = arrayOf(
        Subject("Русский язык","rus"),
        Subject("Физика","phys"),
        Subject("Математика","math"),
        Subject("Английский язык","en"),
        Subject("История","hist")
    ).toList()
}