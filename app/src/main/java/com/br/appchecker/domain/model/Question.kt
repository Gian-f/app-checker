package com.br.appchecker.domain.model

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.br.appchecker.data.remote.response.AnswersData
import kotlinx.parcelize.Parcelize

@Entity(tableName = "questions")
@Parcelize
data class Question(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "answers")
    val answers: List<AnswersData>,
    @ColumnInfo(name = "optionIdResultId")
    val optionIdResultId: Int? = null,
    @ColumnInfo(name = "selected_answer_position")
    var selectedAnswerPosition: Int? = RecyclerView.NO_POSITION
) : Parcelable {
    constructor() : this(0, "", "", emptyList(), null, RecyclerView.NO_POSITION)
}