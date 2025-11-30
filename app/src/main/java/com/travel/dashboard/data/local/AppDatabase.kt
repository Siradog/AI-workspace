package com.travel.dashboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.travel.dashboard.data.local.converters.Converters
import com.travel.dashboard.data.local.dao.ScheduleDao
import com.travel.dashboard.data.local.dao.TodoDao
import com.travel.dashboard.data.local.dao.TripDao
import com.travel.dashboard.data.local.entity.Schedule
import com.travel.dashboard.data.local.entity.Todo
import com.travel.dashboard.data.local.entity.Trip

@Database(
    entities = [Trip::class, Schedule::class, Todo::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun todoDao(): TodoDao
}
