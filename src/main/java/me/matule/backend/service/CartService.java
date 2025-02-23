package me.matule.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.CartItemDto;
import me.matule.backend.data.dto.request.CartItemRequest;
import me.matule.backend.data.dto.request.CartUpdateRequest;
import me.matule.backend.data.entity.CartItem;
import me.matule.backend.data.entity.Product;
import me.matule.backend.data.entity.User;
import me.matule.backend.data.mapper.CartMapper;
import me.matule.backend.repository.CartRepository;
import me.matule.backend.repository.ProductRepository;
import me.matule.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    public List<CartItemDto> getAll() {
        List<CartItem> cartItems = cartRepository.findAll();
        return cartItems.stream()
                .map(cartMapper::toCartDto)
                .toList();
    }

    public List<CartItemDto> getByUserId(Long id) {
        if (userRepository.findById(id).isEmpty())
        {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(id));
        }
        List<CartItem> cartItemList = cartRepository.findByUser_Id(id);
        return cartItemList.stream().map(cartMapper::toCartDto).toList();
    }

    public CartItemDto addToCart(CartItemRequest dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(userId)));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id `%s` not found.".formatted(dto.getProductId())));
        CartItem cartItem = cartRepository.findByUser_IdAndProduct_Id(user.getId(),product.getId())
                .orElse(CartItem.builder()
                        .user(user)
                        .product(product)
                        .build());
        cartItem.setQuantity(cartItem.getQuantity()+dto.getQuantity());
        CartItem resultCartItem = cartRepository.save(cartItem);
        return cartMapper.toCartDto(resultCartItem);
    }

    public Optional<CartItemDto> patch(Long productId, Long userId, CartUpdateRequest request) {
        if (userRepository.findById(userId).isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(userId));
        }

        List<CartItem> cartItems = cartRepository.findByUser_Id(userId);

        CartItem cartItem = cartItems.stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found in cart of user '%s'".formatted(productId,userId)));

        if (request.getQuantity() <=0)
        {
            cartRepository.delete(cartItem);
            return Optional.empty();
        }
        cartItem.setQuantity(request.getQuantity());
        CartItem resultCartItem = cartRepository.save(cartItem);
        return Optional.of(cartMapper.toCartDto(resultCartItem));
    }

    public void delete(Long productId, Long userId) {
        if (userRepository.findById(userId).isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(userId));
        }
        List<CartItem> cartItems = cartRepository.findByUser_Id(userId);
        Optional<CartItem> cartItem = cartItems.stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        cartItem.ifPresent(cartRepository::delete);
    }

    public void clear(Long userId) {
        if (userRepository.findById(userId).isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(userId));
        }
        List<CartItem> cartItems = cartRepository.findByUser_Id(userId);
        if (cartItems.isEmpty()) {
            return;
        }
        cartItems.forEach(cartRepository::delete);
    }
}
