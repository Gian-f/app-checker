package com.br.appchecker.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.br.appchecker.data.remote.response.AnswersData

@Entity(
    tableName = "question_answers_join", indices = [Index("answerId")],
    primaryKeys = ["questionId", "answerId"],
    foreignKeys = [
        ForeignKey(
            entity = Question::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnswersData::class,
            parentColumns = ["id"],
            childColumns = ["answerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuestionAnswersRelation(
    val questionId: Int,
    val answerId: Int,
)
