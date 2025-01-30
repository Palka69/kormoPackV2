package com.example.kormopack.domain.settingsfrag

import retrofit2.Response

class AddNewBrandUseCase(private val repository: BrandRepository) {
    suspend fun addNewBrand(brand: String): Response<Unit>? {
        var result: Response<Unit>? = null
        val list = repository.getFeedBrands()

        if (list.isNotEmpty() && (!list.map { it?.name }.contains(brand))) {
            result = repository.insertBrand(brand)
        }
        return result
    }
}
