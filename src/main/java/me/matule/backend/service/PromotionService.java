package me.matule.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.PromotionDto;
import me.matule.backend.data.entity.Promotion;
import me.matule.backend.data.mapper.PromotionMapper;
import me.matule.backend.repository.PromotionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PromotionService {

    private final PromotionMapper promotionMapper;

    private final PromotionRepository promotionRepository;

    private final ObjectMapper objectMapper;

    public List<PromotionDto> getAll() {
        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream()
                .map(promotionMapper::toPromotionDto)
                .toList();
    }

    public PromotionDto getOne(Long id) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(id);
        return promotionMapper.toPromotionDto(promotionOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<PromotionDto> getMany(List<Long> ids) {
        List<Promotion> promotions = promotionRepository.findAllById(ids);
        return promotions.stream()
                .map(promotionMapper::toPromotionDto)
                .toList();
    }

    public PromotionDto create(PromotionDto dto) {
        Promotion promotion = promotionMapper.toEntity(dto);
        Promotion resultPromotion = promotionRepository.save(promotion);
        return promotionMapper.toPromotionDto(resultPromotion);
    }

    public PromotionDto patch(Long id, JsonNode patchNode) throws IOException {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        PromotionDto promotionDto = promotionMapper.toPromotionDto(promotion);
        objectMapper.readerForUpdating(promotionDto).readValue(patchNode);
        promotionMapper.updateWithNull(promotionDto, promotion);

        Promotion resultPromotion = promotionRepository.save(promotion);
        return promotionMapper.toPromotionDto(resultPromotion);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<Promotion> promotions = promotionRepository.findAllById(ids);

        for (Promotion promotion : promotions) {
            PromotionDto promotionDto = promotionMapper.toPromotionDto(promotion);
            objectMapper.readerForUpdating(promotionDto).readValue(patchNode);
            promotionMapper.updateWithNull(promotionDto, promotion);
        }

        List<Promotion> resultPromotions = promotionRepository.saveAll(promotions);
        return resultPromotions.stream()
                .map(Promotion::getId)
                .toList();
    }

    public PromotionDto delete(Long id) {
        Promotion promotion = promotionRepository.findById(id).orElse(null);
        if (promotion != null) {
            promotionRepository.delete(promotion);
        }
        return promotionMapper.toPromotionDto(promotion);
    }

    public void deleteMany(List<Long> ids) {
        promotionRepository.deleteAllById(ids);
    }
}
