package com.example.suruchat_app.data.db.daos

import androidx.room.*
import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.UserChatWithMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(vararg messages: Message)

    @Transaction
    @Query("SELECT * FROM user_chat")
    fun getUserChatWithMessages(): Flow<List<UserChatWithMessage>>

    @Transaction
    @Query("SELECT * FROM user_chat WHERE chat_id = :chatId")
    suspend fun getMessageByChatId(chatId: String): UserChatWithMessage
}