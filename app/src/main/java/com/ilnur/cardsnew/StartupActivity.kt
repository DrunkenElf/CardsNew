package com.ilnur.cardsnew

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.ilnur.cardsnew.database.AppDatabase
import com.ilnur.cardsnew.database.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class StartupActivity : AppCompatActivity() {

    @Inject lateinit var db: AppDatabase

    fun isLogged() {
        lifecycleScope.launch(Dispatchers.IO){
            _user.postValue(db.userDao().getUserList().firstOrNull())

        }
        user.observe(this, {
            val tmp = it
            if (tmp == null) {
                startLoginAct()
            } else if (tmp.session_id != null && tmp.password != null && tmp.logged) {
                Log.d("Used data exist", tmp.toString())
                //start main
                startMainActivity(tmp)
            } else
                startLoginAct()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        isLogged()
    }

    private fun startMainActivity(user: User){
        Log.d("startMain", user.toString())
        val startupIntent = Intent(this, MainActivity::class.java)
        startActivity(startupIntent)
        this.finish()
    }

    private fun startLoginAct() {
        Log.d("startLogin", "no user")
        val startupIntent = Intent(this, LoginActivity::class.java)
        startActivity(startupIntent)
        this.finish()
    }

    private val _user = MutableLiveData<User?>()

    val user: LiveData<User?> get() = _user

}
