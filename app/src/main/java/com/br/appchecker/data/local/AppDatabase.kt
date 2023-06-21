package com.br.appchecker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.br.appchecker.data.local.converter.Converters
import com.br.appchecker.data.local.dao.AnswerDao
import com.br.appchecker.data.local.dao.QuestionDao
import com.br.appchecker.data.local.dao.UserDao
import com.br.appchecker.data.remote.response.AnswersData
import com.br.appchecker.domain.model.Question
import com.br.appchecker.domain.model.QuestionAnswersRelation
import com.br.appchecker.domain.model.User

@Database(
    entities = [
        User::class,
        AnswersData::class,
        Question::class,
        QuestionAnswersRelation::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun answerDao(): AnswerDao

    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appDatabase.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}