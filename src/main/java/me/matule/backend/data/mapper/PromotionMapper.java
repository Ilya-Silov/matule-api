package me.matule.backend.data.mapper;

import me.matule.backend.data.dto.PromotionDto;
import me.matule.backend.data.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PromotionMapper {
    Promotion toEntity(PromotionDto promotionDto);

    PromotionDto toPromotionDto(Promotion promotion);

    Promotion updateWithNull(PromotionDto promotionDto, @MappingTarget Promotion promotion);
}