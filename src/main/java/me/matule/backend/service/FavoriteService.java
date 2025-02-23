package me.matule.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.FavoriteDto;
import me.matule.backend.data.dto.request.FavoriteItemRequest;
import me.matule.backend.data.entity.Favorite;
import me.matule.backend.data.entity.Product;
import me.matule.backend.data.entity.User;
import me.matule.backend.data.mapper.FavoriteMapper;
import me.matule.backend.repository.FavoriteRepository;
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
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private final ProductRepository productRepository;

    public List<FavoriteDto> getAll(Long userId) {
        if (userRepository.findById(userId).isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(userId));
        }
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream()
                .map(favoriteMapper::toFavoriteDto)
                .toList();
    }

    public FavoriteDto create(Long userId, FavoriteItemRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(userId)));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id `%s` not found.".formatted(dto.getProductId())));
        if (favoriteRepository.findByUser_IdAndProduct_Id(userId, dto.getProductId()).isPresent())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite with product id `%s` already exist for user '%s'.".formatted(dto.getProductId(),userId));
        }
        Favorite favorite = Favorite.builder()
                .user(user)
                .product(product)
                .build();
        Favorite resultFavorite = favoriteRepository.save(favorite);
        return favoriteMapper.toFavoriteDto(resultFavorite);
    }

    public void delete(Long productId, Long userId) {
        if (userRepository.findById(userId).isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id `%s` not found.".formatted(userId));
        }
        Favorite favorite = favoriteRepository.findByUser_IdAndProduct_Id(userId, productId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite with user_id `%s` and product_id '%s' not found".formatted(userId, productId)));
        favoriteRepository.delete(favorite);
    }
}
