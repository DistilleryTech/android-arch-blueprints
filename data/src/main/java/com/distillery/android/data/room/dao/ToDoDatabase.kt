package com.distillery.android.data.room.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.distillery.android.data.room.models.ToDoEntity

@Database(entities = [ToDoEntity::class], version = 1)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun dao(): ToDoDao

    companion object {
        fun create(context: Context): ToDoDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ToDoDatabase::class.java,
                "ToDoDatabase"
            ) // allow main queties for debug only, to be removed
                .allowMainThreadQueries()
                .build()
        }
    }
}
