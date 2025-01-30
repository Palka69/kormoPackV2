package com.example.kormopack.data.specsdatabase.mapper

import com.example.kormopack.data.specsdatabase.SpecificationFeedEntity
import com.example.kormopack.domain.specsfrag.model.SpecificationFeedModel

object SpecificationFeedMapper {
    fun SpecificationFeedEntity.toModel(): SpecificationFeedModel {
        return SpecificationFeedModel(
            spec_id = this.spec_id,
            spec_num = this.spec_num,
            recipe_num = this.recipe_num,
            feed_name = this.feed_name,
            brand_name = this.brand_name,
            total_weight = this.total_weight,
            pieces_perc = this.pieces_perc,
            sauce_perc = this.sauce_perc,
            addition_one_perc = this.addition_one_perc,
            addition_two_perc = this.addition_two_perc,
            matrix_type = this.matrix_type,
            reg_data = this.reg_data,
        )
    }


    fun SpecificationFeedModel.toEntity(): SpecificationFeedEntity {
        return SpecificationFeedEntity(
            spec_id = this.spec_id,
            spec_num = this.spec_num,
            recipe_num = this.recipe_num,
            feed_name = this.feed_name,
            brand_name = this.brand_name,
            total_weight = this.total_weight,
            pieces_perc = this.pieces_perc,
            sauce_perc = this.sauce_perc,
            addition_one_perc = this.addition_one_perc,
            addition_two_perc = this.addition_two_perc,
            matrix_type = this.matrix_type,
            reg_data = this.reg_data,
        )
    }

}