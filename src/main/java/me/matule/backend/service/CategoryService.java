package me.matule.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.CategoryDto;
import me.matule.backend.data.dto.ProductDto;
import me.matule.backend.data.entity.Category;
import me.matule.backend.data.mapper.CategoryMapper;
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
public class CategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    private final ObjectMapper objectMapper;

    public List<CategoryDto> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toCategoryDto)
                .toList();
    }

    public CategoryDto getOne(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryMapper.toCategoryDto(categoryOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public CategoryDto create(CategoryDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Category resultCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(resultCategory);
    }

    public CategoryDto patch(Long id, JsonNode patchNode) throws IOException {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);
        objectMapper.readerForUpdating(categoryDto).readValue(patchNode);
        categoryMapper.updateWithNull(categoryDto, category);

        Category resultCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(resultCategory);
    }

    public CategoryDto delete(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            categoryRepository.delete(category);
        }
        return categoryMapper.toCategoryDto(category);
    }
}
