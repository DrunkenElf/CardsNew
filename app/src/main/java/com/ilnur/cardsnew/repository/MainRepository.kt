package com.ilnur.cardsnew.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ilnur.cardsnew.database.*
import javax.inject.Inject


open class MainRepository @Inject constructor(
    var context: Context,
    var userDao: UserDao, var subjectDao: SubjectDao
) {


    val db = AppDatabase(context)
    val currentSubj = MutableLiveData<Subject>()


    fun getSubjects(): List<Subject> = subjectDao.getSubjects()

    /*fun updateSubjects(href: String): List<Subject> {
        re
    }*/

    fun selectSubject(subject: Subject) {
        currentSubj.postValue(subject)
    }


    fun getUserDb(): LiveData<List<User>> {
        return userDao.getUser()
    }


}
