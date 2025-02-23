package me.matule.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

/**
 * DTO for {@link me.matule.backend.data.entity.OrderItem}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    ProductDto product;
    int quantity;
    BigDecimal price;
}