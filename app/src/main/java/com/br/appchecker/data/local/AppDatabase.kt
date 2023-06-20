package com.br.appchecker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.br.appchecker.data.local.dao.AnswerDao
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.remote.response.AnswersData
import com.br.appchecker.domain.model.User

@Database(
    entities = [
        User::class,
        AnswersData::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun answerDao(): AnswerDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDatabase? = null

        fun getInstance(context: Context): RoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appDatabase.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}