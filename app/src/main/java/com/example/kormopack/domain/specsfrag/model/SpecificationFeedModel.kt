package com.example.kormopack.domain.specsfrag.model

data class SpecificationFeedModel(
    val spec_id: Int,
    val spec_num: String,
    val recipe_num: Int,
    val feed_name: String,
    val brand_name: String,
    val total_weight: Int,
    val pieces_perc: Int,
    val sauce_perc: Int,
    val addition_one_perc: Int,
    val addition_two_perc: Int,
    val matrix_type: String,
    val reg_data: String,
)
