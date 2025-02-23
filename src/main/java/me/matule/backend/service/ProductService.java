package me.matule.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.ProductDto;
import me.matule.backend.data.entity.Product;
import me.matule.backend.data.mapper.ProductMapper;
import me.matule.backend.repository.CategoryRepository;
import me.matule.backend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    public List<ProductDto> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toProductDto)
                .toList();
    }

    public ProductDto getOne(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productMapper.toProductDto(productOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public ProductDto create(ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        Product resultProduct = productRepository.save(product);
        return productMapper.toProductDto(resultProduct);
    }

    public ProductDto patch(Long id, JsonNode patchNode) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        ProductDto productDto = productMapper.toProductDto(product);
        objectMapper.readerForUpdating(productDto).readValue(patchNode);
        productMapper.updateWithNull(productDto, product);

        Product resultProduct = productRepository.save(product);
        return productMapper.toProductDto(resultProduct);
    }

    public ProductDto delete(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.delete(product);
        }
        return productMapper.toProductDto(product);
    }

    public List<ProductDto> getByCategory(Long categoryId)
    {
        return productRepository.findByCategories_Id(categoryId).stream().map(productMapper::toProductDto).toList();
    }
}
