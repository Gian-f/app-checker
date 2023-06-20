package com.br.appchecker.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class AnswersData(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("idQuestion")
    val idQuestion: Int,
    @SerializedName("positionOrder")
    val positionOrder: Int
) : Parcelable {
    override fun toString(): String {
        return description
    }
}