package me.matule.backend.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.matule.backend.data.dto.ProductDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    Long productId;
    int quantity;
}
