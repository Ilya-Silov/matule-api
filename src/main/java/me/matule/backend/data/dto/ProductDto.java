package me.matule.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link me.matule.backend.data.entity.Product}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    Long id;
    String name;
    String description;
    BigDecimal price;
    List<CategoryDto> categories;
    List<String> images;
}