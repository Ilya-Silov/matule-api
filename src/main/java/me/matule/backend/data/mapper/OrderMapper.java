package me.matule.backend.data.mapper;

import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.OrderDto;
import me.matule.backend.data.dto.request.OrderItemRequest;
import me.matule.backend.data.dto.request.OrderRequest;
import me.matule.backend.data.entity.Order;
import me.matule.backend.data.entity.OrderItem;
import me.matule.backend.data.entity.Product;
import me.matule.backend.data.entity.User;
import me.matule.backend.repository.ProductRepository;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {OrderItemMapper.class})
public interface OrderMapper {

    Order toEntity(OrderDto orderDto);

    @AfterMapping
    default void linkItems(@MappingTarget Order order) {
        order.getItems().forEach(item -> item.setOrder(order));
    }

    OrderDto toOrderDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    Order toEntity(OrderRequest orderRequest);

    Order updateWithNull(OrderDto orderDto, @MappingTarget Order order);

}