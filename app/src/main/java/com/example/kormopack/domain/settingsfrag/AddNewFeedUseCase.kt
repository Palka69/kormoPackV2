package com.example.kormopack.domain.settingsfrag

import com.example.kormopack.domain.specsfrag.model.SpecificationFeedModel
import com.example.kormopack.server.ApiService
import retrofit2.Response

class AddNewFeedUseCase(private val apiService: ApiService) {
    suspend fun addNewFeed(spec: SpecificationFeedModel): Response<Unit> {
        return apiService.addFeed(feedToString(spec))
    }

    private fun feedToString(spec: SpecificationFeedModel): String {
        return "${spec.spec_id}/${spec.spec_num}/${spec.recipe_num}/${spec.feed_name}/${spec.brand_name}/${spec.total_weight}/${spec.pieces_perc}/${spec.sauce_perc}/${spec.addition_one_perc}/${spec.addition_two_perc}/${spec.matrix_type}/${spec.reg_data}"
    }
}