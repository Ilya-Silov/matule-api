package me.matule.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.OrderDto;
import me.matule.backend.data.dto.request.OrderRequest;
import me.matule.backend.data.entity.Order;
import me.matule.backend.data.entity.User;
import me.matule.backend.data.mapper.CustomOrderMapper;
import me.matule.backend.data.mapper.OrderMapper;
import me.matule.backend.repository.OrderRepository;
import me.matule.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final CustomOrderMapper customOrderMapper;

    public List<OrderDto> getAll(Long userId) {
        if (userRepository.findById(userId).isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(userId));
        }
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toOrderDto).toList();
    }

    public OrderDto getOne(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderMapper.toOrderDto(orderOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public OrderDto create(OrderRequest dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with id `%s` not found".formatted(userId)
                ));

        Order order = customOrderMapper.toEntity(dto, user);
        Order resultOrder = orderRepository.save(order);
        return orderMapper.toOrderDto(resultOrder);
    }


    public OrderDto delete(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
        }
        return orderMapper.toOrderDto(order);
    }

    public boolean hasRights(Long userId, Long orderId)
    {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found".formatted(userId)));
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id `%s` not found".formatted(orderId)));
        return order.getUser().getId().equals(user.getId());
    }

    public void deleteMany(List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }
}
