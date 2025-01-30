package com.example.kormopack.data

import com.example.kormopack.data.specsdatabase.SpecificationBrandEntity
import com.example.kormopack.domain.settingsfrag.BrandRepository
import com.example.kormopack.server.ApiService
import retrofit2.Response

class BrandRepositoryImpl(private val apiService: ApiService) : BrandRepository {
    override suspend fun insertBrand(brand: String): Response<Unit> {
        return apiService.addBrand(brand)
    }

    override suspend fun getFeedBrands(): List<SpecificationBrandEntity?> {
        return apiService.getFeedBrands()
    }
}
