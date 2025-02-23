package me.matule.backend.data.mapper;

import me.matule.backend.data.dto.FavoriteDto;
import me.matule.backend.data.entity.Favorite;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ProductMapper.class})
public interface FavoriteMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product", target = "product")
    FavoriteDto toFavoriteDto(Favorite favorite);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "product", target = "product")
    Favorite toEntity(FavoriteDto favoriteDto);

    Favorite updateWithNull(FavoriteDto favoriteDto, @MappingTarget Favorite favorite);
}