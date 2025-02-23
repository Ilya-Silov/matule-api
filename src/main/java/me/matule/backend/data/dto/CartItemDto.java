package me.matule.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.matule.backend.data.entity.CartItem;

/**
 * DTO for {@link CartItem}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    Long id;
    ProductDto product;
    int quantity;
}