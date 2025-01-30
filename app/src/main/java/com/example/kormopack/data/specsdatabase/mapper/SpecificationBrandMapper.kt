package com.example.kormopack.data.specsdatabase.mapper

import com.example.kormopack.data.specsdatabase.SpecificationBrandEntity
import com.example.kormopack.domain.specsfrag.model.SpecificationBrandModel

object SpecificationBrandMapper {
    fun SpecificationBrandEntity.toModel(): SpecificationBrandModel {
        return SpecificationBrandModel(
            feed_id = this.feed_id,
            name = this.name,
            brandLogo = this.brand_logo,
            cardBackColor = this.cardBackColor,
            textColor = this.textColor
        )
    }

    fun SpecificationBrandModel.toEntity(): SpecificationBrandEntity {
        return SpecificationBrandEntity(
            feed_id = this.feed_id,
            name = this.name,
            brand_logo = this.brandLogo,
            cardBackColor = this.cardBackColor,
            textColor = this.textColor
        )
    }
}
