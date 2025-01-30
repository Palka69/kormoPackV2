package com.example.kormopack.domain.settingsfrag

import com.example.kormopack.data.specsdatabase.mapper.SpecificationBrandMapper.toModel
import com.example.kormopack.domain.specsfrag.model.SpecificationBrandModel
import com.example.kormopack.server.ApiService

class GetAllBrandUseCase(private val apiService: ApiService) {
    suspend fun getAllBrands(): List<SpecificationBrandModel> {
        return apiService.getFeedBrands().map { it.toModel() }
    }
}