package com.ilnur.cardsnew.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ilnur.cardsnew.utils.subjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [User::class, Category::class, Card::class, Subject::class], version = 2)
abstract class AppDatabase : RoomDatabase(){

    abstract fun userDao(): UserDao

    abstract fun categoryDao(): CategoryDao

    abstract fun cardDao(): CardDao

    abstract fun subjectDao(): SubjectDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "cards.db")
            .addCallback(object : Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        subjects.forEach { instance?.subjectDao()?.insert(it) }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            //.allowMainThreadQueries()
            .build()
    }



}