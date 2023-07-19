package com.br.appchecker.data.remote.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "answers")
@Parcelize
data class AnswersData(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "questionId")
    val questionId: Int,
    @ColumnInfo(name = "positionOrder")
    val positionOrder: Int
): Parcelable {

    constructor() : this(0, "", 0, 0)
    override fun toString(): String {
        return description
    }
}