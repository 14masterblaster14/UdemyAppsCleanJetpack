package com.example.room.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriberDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubscriber(subscriber: Subscriber): Long //  return type will be Array, Long or listof(Long)
    //It will return the Row Id , if error occurs then it will return -1

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber): Int // // Int -> number of rows updated

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber): Int // Int -> number of rows deleted

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll(): Int  // // Int -> number of rows deleted

    @Query("SELECT * FROM subscriber_data_table")
    // fun getAllSubscribers(): LiveData<List<Subscriber>>
    fun getAllSubscribers(): Flow<List<Subscriber>> // Use Flow instead of LiveData (2021 Update)
}