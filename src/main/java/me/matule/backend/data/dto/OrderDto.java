package me.matule.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link me.matule.backend.data.entity.Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    Long id;
    LocalDateTime orderDate;
    BigDecimal totalPrice;
    String shippingAddress;
    String contactPhone;
    List<OrderItemDto> items;
}