package com.example.kormopack.data.specsdatabase


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: SpecificationBrandEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(feed: SpecificationFeedEntity)

    @Query("SELECT * FROM feed_specifications WHERE brand_name = :brand")
    suspend fun getFeedByBrand(brand: String): List<SpecificationFeedEntity>

    @Query("SELECT * FROM feed_brands")
    suspend fun getFeedBrands(): List<SpecificationBrandEntity?>

    @Query("SELECT * FROM feed_specifications WHERE brand_name = :brand AND feed_name LIKE '%' || :keyWord || '%'")
    suspend fun getFeedWhichContains(brand: String, keyWord: String): List<SpecificationFeedEntity>
}