package com.example.suruchat_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.suruchat_app.data.db.daos.MessageDao
import com.example.suruchat_app.data.db.daos.UserDao
import com.example.suruchat_app.domain.models.User

@Database(entities = [User::class],version = 1)
abstract class SuruChatDatabase: RoomDatabase() {

    abstract val userDao: UserDao

    abstract val messageDao: MessageDao

    companion object{

        val DATABASE_NAME = "suruchat_db"
    }
}