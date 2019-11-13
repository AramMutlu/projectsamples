package com.example.workr.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "notificationName") val notificationName: String?,
    @ColumnInfo(name = "isOn") val isOn: Boolean?

)