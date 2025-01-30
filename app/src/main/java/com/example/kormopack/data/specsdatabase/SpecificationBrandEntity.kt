package com.example.kormopack.data.specsdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_brands")
data class SpecificationBrandEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "feed_id")
    val feed_id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "brand_logo")
    val brand_logo: Int,

    @ColumnInfo(name = "cardBackColor")
    val cardBackColor: Int,

    @ColumnInfo(name = "textColor")
    val textColor: Int
)
