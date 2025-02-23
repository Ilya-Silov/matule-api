package me.matule.backend.data.mapper;

import me.matule.backend.data.dto.CartItemDto;
import me.matule.backend.data.dto.request.CartItemRequest;
import me.matule.backend.data.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface CartMapper {
    CartItem toEntity(CartItemDto cartItemDto);

    CartItemDto toCartDto(CartItem cartItem);

    CartItem updateWithNull(CartItemDto cartItemDto, @MappingTarget CartItem cartItem);

    CartItem toEntity(CartItemRequest cartItemDto);

    CartItem updateWithNull(CartItemRequest cartItemDto, @MappingTarget CartItem cartItem);
}