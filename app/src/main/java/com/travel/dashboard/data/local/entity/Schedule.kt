package com.travel.dashboard.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

enum class Genre {
    TRANSPORT, // 移動
    SIGHTSEEING, // 観光
    MEAL // 食事
}

@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tripId"])]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tripId: Int,
    val time: LocalDateTime,
    val place: String,
    val genre: Genre,
    val memo: String? = null,
    val transportDetails: String? = null // 座席番号など
)
