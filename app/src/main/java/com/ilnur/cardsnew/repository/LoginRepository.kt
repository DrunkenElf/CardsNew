package com.ilnur.cardsnew.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.ilnur.cardsnew.database.AppDatabase
import com.ilnur.cardsnew.database.User
import com.ilnur.cardsnew.database.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
class LoginRepositoryCom: KoinComponent {
    val loginRepository by inject<LoginRepository>()
}
*/


class LoginRepository @Inject constructor(var context: Context, var userDao: UserDao){
    var db: AppDatabase = AppDatabase(context)


    //fun getDB(context: Context) = AppDatabase(context)

     fun getUserDb(): LiveData<User> {
        return userDao.getUser()
    }

     fun addOrUpdateUser(user: User) {
        Log.d("addOrUpdate", user.toString())
        CoroutineScope(Dispatchers.Main).launch { addOrUpdateUserBg(user) }
    }

    private suspend fun addOrUpdateUserBg(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }



     fun isLogged(): Boolean =
        if (userDao.getUser().value != null) userDao.getUser().value!!.session_id.isNullOrBlank() else false

    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

}

/*
companion object {
    // Singleton instantiation you already know and love
    @Volatile
    private var instance: LoginRepository? = null

    fun getInstance(quoteDao: UserDao) =
        instance ?: synchronized(this) {
            instance ?: LoginRepository(quoteDao).also { instance = it }
        }
}*/
