package com.example.kormopack.domain.settingsfrag

import com.example.kormopack.data.specsdatabase.SpecificationBrandEntity
import retrofit2.Response

interface BrandRepository {
    suspend fun insertBrand(brand: String): Response<Unit>
    suspend fun getFeedBrands(): List<SpecificationBrandEntity?>
}
