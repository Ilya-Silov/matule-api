package me.matule.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link me.matule.backend.data.entity.Promotion}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDto {
    String title;
    String description;
    String bannerUrl;
    LocalDateTime startDate;
    LocalDateTime endDate;
}