package me.matule.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * DTO for {@link me.matule.backend.data.entity.Favorite}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDto {
    Long userId;
    ProductDto product;
}