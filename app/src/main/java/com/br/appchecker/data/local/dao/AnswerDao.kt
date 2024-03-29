package com.br.appchecker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.br.appchecker.data.remote.response.AnswersData

@Dao
interface AnswerDao {

    @Query("SELECT * FROM answers ORDER BY positionOrder")
    fun getAll(): List<AnswersData>

    @Query("SELECT * FROM answers WHERE id IN (:AnswerDataIds)")
    fun loadAllByIds(AnswerDataIds: IntArray): List<AnswersData>

    @Query("SELECT * FROM answers WHERE questionId LIKE :id ORDER BY positionOrder LIMIT 1")
    fun findById(id: Int): AnswersData

    @Insert
    fun insertAll(vararg answer: AnswersData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(answer: AnswersData)

    @Delete
    fun delete(answer: AnswersData)

    @Query("DELETE FROM answers")
    fun deleteAll()
}