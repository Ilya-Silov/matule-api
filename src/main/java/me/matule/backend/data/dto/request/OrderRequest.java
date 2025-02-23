package me.matule.backend.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.matule.backend.data.dto.OrderItemDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    String shippingAddress;
    String contactPhone;
    List<OrderItemRequest> items;
}
