package me.matule.backend.autocomplete.service;

import me.matule.backend.autocomplete.dto.ProductDto;

import java.util.List;

public interface ProductAutocompleteService {
    List<ProductDto> getPopularProducts();
}
