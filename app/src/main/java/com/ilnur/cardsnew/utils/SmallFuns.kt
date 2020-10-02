package com.ilnur.cardsnew.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ilnur.cardsnew.database.AppDatabase
import com.ilnur.cardsnew.database.Subject
import com.ilnur.cardsnew.database.UserDao

val subjects: List<Subject> = arrayOf(
    Subject("Русский язык","rus"),
    Subject("Физика","phys"),
    Subject("Математика","math"),
    Subject("Английский язык","en"),
    Subject("История","hist")
).toList()

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}


data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

}
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}