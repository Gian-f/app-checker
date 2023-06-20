package com.br.appchecker.domain.model

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.br.appchecker.data.remote.response.AnswersData
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("answers")
    val answers: List<AnswersData>,
    @ColumnInfo("option_id_result")
    val optionIdResult: AnswersData? = null,
    @ColumnInfo("selected_answer_position")
    var selectedAnswerPosition: Int? = RecyclerView.NO_POSITION
): Parcelable

