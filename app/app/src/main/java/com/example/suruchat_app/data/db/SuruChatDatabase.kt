package com.example.suruchat_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.suruchat_app.data.db.daos.MessageDao
import com.example.suruchat_app.data.db.daos.UserDao
import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.models.UserChat

@Database(entities = [User::class,UserChat::class,Message::class],version = 6)
abstract class SuruChatDatabase: RoomDatabase() {

    abstract val userDao: UserDao

    abstract val messageDao: MessageDao

    companion object{
        const val DATABASE_NAME = "suruchat_db"
    }
}