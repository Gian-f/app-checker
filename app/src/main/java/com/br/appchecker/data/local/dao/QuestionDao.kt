package com.br.appchecker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.br.appchecker.domain.model.Question

@Dao
interface QuestionDao {

    @Insert
    suspend fun insert(question: Question)

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<Question>

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getQuestionById(id: Int): Question

    @Query("SELECT * FROM questions WHERE id IN (SELECT id FROM users WHERE id = :userId)")
    suspend fun getQuestionsByUserId(userId: Int): List<Question>
}