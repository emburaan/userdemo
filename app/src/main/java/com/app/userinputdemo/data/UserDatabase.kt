package com.app.userinputdemo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(User::class)],version = 1,exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: UserDatabase? = null
        fun getDataBase(context: Context): UserDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, UserDatabase::class.java, "user-db")
                    .allowMainThreadQueries().build()
            }
            return INSTANCE as UserDatabase
        }
    }

    abstract fun daoUser(): DaoUser
}