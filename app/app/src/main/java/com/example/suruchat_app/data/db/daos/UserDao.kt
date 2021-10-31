package com.example.suruchat_app.data.db.daos

import androidx.room.*
import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.models.UserChat
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(vararg users: User)

    @Query("SELECT * FROM user_chat")
    fun getUserChats(): Flow<List<UserChat>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserChat(vararg userChats: UserChat)

    @Delete
    suspend fun deleteUser(user: User)
}