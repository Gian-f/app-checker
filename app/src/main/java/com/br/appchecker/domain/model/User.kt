package com.br.appchecker.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey
    @SerializedName("user_id")
    val id: Int = 0,
    @SerializedName("email")
    @ColumnInfo("email")
    val email: String,
    @ColumnInfo("name")
    @SerializedName("nome")
    val name: String
)