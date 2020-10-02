package com.ilnur.cardsnew.repository

import android.content.Context
import com.ilnur.cardsnew.database.CategoryDao

class TopicsRepository(var context: Context, var categoryDao: CategoryDao) {


    suspend fun getTopics(predmet: String, parent_id: Int = 0) = categoryDao.getTopics(predmet, parent_id)
}