package com.br.appchecker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.br.appchecker.domain.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users LIMIT 1")
    fun find(): User?

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE email LIKE :first LIMIT 1")
    fun findByEmail(first: String): User

    @Query("SELECT * FROM users WHERE id LIKE :userId LIMIT 1")
    fun findById(userId: Int): User

    @Query("SELECT id FROM users LIMIT 1")
    suspend fun getLoggedInUserId(): Int

    @Insert
    fun insertAll(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM users")
    fun deleteAll()

}