package com.example.kormopack.data.specsdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_specifications")
data class SpecificationFeedEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "spec_id")
    val spec_id: Int,

    @ColumnInfo(name = "spec_num")
    val spec_num: String,

    @ColumnInfo(name = "recipe_num")
    val recipe_num: Int,

    @ColumnInfo(name = "feed_name")
    val feed_name: String,

    @ColumnInfo(name = "brand_name")
    val brand_name: String,

    @ColumnInfo(name = "total_weight")
    val total_weight: Int,

    @ColumnInfo(name = "pieces_perc")
    val pieces_perc: Int,

    @ColumnInfo(name = "sauce_perc")
    val sauce_perc: Int,

    @ColumnInfo(name = "addition_one_perc")
    val addition_one_perc: Int = 0,

    @ColumnInfo(name = "addition_two_perc")
    val addition_two_perc: Int = 0,

    @ColumnInfo(name = "matrix_type")
    val matrix_type: String,

    @ColumnInfo(name = "reg_data")
    val reg_data: String,

)

