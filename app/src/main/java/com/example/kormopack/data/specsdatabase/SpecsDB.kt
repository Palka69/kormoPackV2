package com.example.kormopack.data.specsdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SpecificationFeedEntity::class, SpecificationBrandEntity::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class SpecsDB : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        fun getDb(context: Context): SpecsDB {
            return Room.databaseBuilder(
                context.applicationContext,
                SpecsDB::class.java,
                "specs.db"
            ).createFromAsset("database/specifications.db")
                .build()
        }
    }
}
