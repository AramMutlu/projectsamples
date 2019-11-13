package com.example.workr.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

//Functions to use the local device database
@Dao
interface NotificantionDao {
    @Query("SELECT * FROM notifications")
    fun getAll(): List<Notification>

    @Query("SELECT * FROM notifications WHERE id IN (:notificationIds)")
    fun loadAllByIds(notificationIds: IntArray): List<Notification>

    @Insert(onConflict = REPLACE)
    fun insert(notification: Notification)

    @Delete
    fun delete(notification: Notification)
}