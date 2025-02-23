package me.matule.backend.data.mapper;

import me.matule.backend.data.dto.request.OrderItemRequest;
import me.matule.backend.data.dto.request.OrderRequest;
import me.matule.backend.data.entity.Order;
import me.matule.backend.data.entity.OrderItem;
import me.matule.backend.data.entity.Product;
import me.matule.backend.data.entity.User;
import me.matule.backend.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomOrderMapper {
    private final ProductRepository productRepository;

    public CustomOrderMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Order toEntity(OrderRequest request, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setContactPhone(request.getContactPhone());

        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> toOrderItem(itemRequest, order))
                .collect(Collectors.toList());

        order.setItems(orderItems);
        return order;
    }

    private OrderItem toOrderItem(OrderItemRequest request, Order order) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + request.getProductId()));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));

        return item;
    }
}
