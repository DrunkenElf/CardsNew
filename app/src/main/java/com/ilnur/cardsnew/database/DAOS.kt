package com.ilnur.cardsnew.database


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE login = :login")
    fun getByLogin(login: String): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE session_id = :session_id")
    fun getBySessionId(session_id: String): LiveData<List<User>>

    @Query("SELECT * FROM user")
     fun getUser(): LiveData<List<User>>

    @Query("SELECT * FROM user")
     fun getUserList(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}


@Dao
interface CategoryDao {
    //@Query("SELECT title, \"order\" FROM " + predmet + "_cat" + " WHERE parent_id = :")
    /*@Query("SELECT * FROM category WHERE parent_id IN (SELECT parent_id FROM category " +
            "WHERE parent_id =:parent_id) AND subj " +
            "IN (SELECT subj FROM category WHERE subj=:predmet)")*/
    @Query("SELECT * FROM category WHERE subj =:predmet AND parent_id =:parent_id")
    fun getTopics(predmet: String, parent_id: Int = 0): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category)

}

@Dao
interface CardDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card: Card)
}

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subject")
    fun getSubjects(): List<Subject>

    @Query("SELECT * FROM subject WHERE href = :href LIMIT 1")
    fun getSubject(href: String): Subject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(subject: Subject)

    @Delete
    fun delete(subject: Subject)
}


fun getSubj(subj: String): String {
    return when (subj) {
        "Русский язык" -> "rus"
        else -> ""
    }
}