package me.matule.backend.data.mapper;

import me.matule.backend.data.dto.OrderItemDto;
import me.matule.backend.data.dto.request.OrderItemRequest;
import me.matule.backend.data.dto.request.OrderRequest;
import me.matule.backend.data.entity.Order;
import me.matule.backend.data.entity.OrderItem;
import me.matule.backend.data.entity.Product;
import me.matule.backend.data.entity.User;
import me.matule.backend.repository.ProductRepository;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemDto orderItemDto);

    OrderItemDto toOrderItemDto(OrderItem orderItem);

    OrderItem updateWithNull(OrderItemDto orderItemDto, @MappingTarget OrderItem orderItem);


}