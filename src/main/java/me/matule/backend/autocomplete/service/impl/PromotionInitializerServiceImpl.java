package me.matule.backend.autocomplete.service.impl;

import jakarta.annotation.PostConstruct;
import me.matule.backend.autocomplete.service.PromotionInitializerService;
import me.matule.backend.data.entity.Promotion;
import me.matule.backend.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionInitializerServiceImpl implements PromotionInitializerService {

    private final PromotionRepository promotionRepository;

    public PromotionInitializerServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @PostConstruct
    public void init() {
        checkAndInitializePromotions();
    }

    @Override
    public void initializePromotions() {
        List<Promotion> promotions = new ArrayList<>();

        Promotion promo1 = new Promotion();
        promo1.setTitle("Зимняя распродажа");
        promo1.setDescription("Скидки до 50% на зимнюю коллекцию обуви!");
        promo1.setBannerUrl("/images/0xcs77qz84w3mbxtotxx5bn5qau1p41w");
        promo1.setStartDate(LocalDateTime.now().minusDays(1));
        promo1.setEndDate(LocalDateTime.now().plusDays(14));
        promotions.add(promo1);

        Promotion promo2 = new Promotion();
        promo2.setTitle("Чёрная пятница");
        promo2.setDescription("Только в эту пятницу — скидки до 70% на всё!");
        promo2.setBannerUrl("/images/promo_black_friday");
        promo2.setStartDate(LocalDateTime.now().plusDays(2));
        promo2.setEndDate(LocalDateTime.now().plusDays(3));
        promotions.add(promo2);

        Promotion promo3 = new Promotion();
        promo3.setTitle("Бесплатная доставка");
        promo3.setDescription("Бесплатная доставка при заказе от 5000 рублей.");
        promo3.setBannerUrl("/images/promo_free_shipping");
        promo3.setStartDate(LocalDateTime.now());
        promo3.setEndDate(LocalDateTime.now().plusMonths(1));
        promotions.add(promo3);

        promotionRepository.saveAll(promotions);
    }

    private void checkAndInitializePromotions() {
        if (isDatabaseEmpty()) {
            initializePromotions();
        } else {
            System.out.println("Database already contains promotions, skipping initialization.");
        }
    }

    private boolean isDatabaseEmpty() {
        return promotionRepository.count() == 0;
    }
}