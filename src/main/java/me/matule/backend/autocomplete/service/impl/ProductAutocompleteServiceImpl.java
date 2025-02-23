package me.matule.backend.autocomplete.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.matule.backend.autocomplete.dto.ProductDto;
import me.matule.backend.autocomplete.service.ProductAutocompleteService;
import me.matule.backend.data.entity.Category;
import me.matule.backend.data.entity.Product;
import me.matule.backend.repository.CategoryRepository;
import me.matule.backend.repository.ProductRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductAutocompleteServiceImpl implements ProductAutocompleteService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIR = "./data/images/";
    private static final String IMAGE_API_PREFIX = "/images/";


    @PostConstruct
    public void init() {
        File imageDir = new File(IMAGE_DIR);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        if (productRepository.count() == 0) {
            populateDatabaseFromApi();
        }
    }

    @Override
    public List<ProductDto> getPopularProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private void populateDatabaseFromApi() {
        String url = "https://api.retailrocket.ru/api/2.0/recommendation/popular/55379e776636b417f47acd68/" +
                "?categoryIds=2&session=67b9a9dd1f7040b1fcb9520e&pvid=660521327508981&isDebug=false&format=json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "*/*");
        headers.set("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6,zh;q=0.5");
        headers.set("origin", "https://sneakerhead.ru");
        headers.set("priority", "u=1, i");
        headers.set("referer", "https://sneakerhead.ru/");
        headers.set("sec-ch-ua", "\"Not(A:Brand\";v=\"99\", \"Google Chrome\";v=\"133\", \"Chromium\";v=\"133\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"Windows\"");
        headers.set("sec-fetch-dest", "empty");
        headers.set("sec-fetch-mode", "cors");
        headers.set("sec-fetch-site", "cross-site");
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            List<?> rawList = objectMapper.readValue(response.getBody(), List.class);
            List<Product> products = new ArrayList<>();

            for (Object item : rawList) {
                Map<String, Object> apiResponse = (Map<String, Object>) item;
                Product product = mapApiResponseToProduct(apiResponse);
                products.add(product);
            }

            productRepository.saveAll(products);
        } catch (Exception e) {
            throw new RuntimeException("Failed to populate database from API", e);
        }
    }

    private Product mapApiResponseToProduct(Map<String, Object> apiResponse) {
        List<String> categoryNames = (List<String>) apiResponse.get("CategoryNames");
        List<Category> categories = categoryNames.stream()
                .map(name -> categoryRepository.findByName(name)
                        .orElseGet(() -> categoryRepository.save(
                                new Category(null, name, new ArrayList<>()))))
                .collect(Collectors.toList());

        List<String> imageUrls = new ArrayList<>();
        String pictureUrl = (String) apiResponse.get("PictureUrl");
        if (pictureUrl != null) {
            String imageId = downloadImageAndGetId(pictureUrl);
            if (imageId != null) {
                imageUrls.add(IMAGE_API_PREFIX + imageId);
                imageUrls.add(IMAGE_API_PREFIX + imageId);
                imageUrls.add(IMAGE_API_PREFIX + imageId);
            }
        }

        Product product = new Product();
        product.setName((String) apiResponse.get("Name"));
        product.setDescription((String) apiResponse.get("Description"));
        product.setPrice(BigDecimal.valueOf(Double.parseDouble(apiResponse.get("Price").toString())));
        product.setCategories(categories);
        product.setImages(imageUrls);
        return product;
    }

    private String downloadImageAndGetId(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String fileName = UUID.randomUUID().toString() + "_" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            String localPath = IMAGE_DIR + fileName;

            try (InputStream in = url.openStream();
                 FileOutputStream out = new FileOutputStream(localPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            // Возвращаем только UUID как ID изображения
            return fileName.split("_")[0];
        } catch (Exception e) {
            System.err.println("Failed to download image: " + imageUrl + " - " + e.getMessage());
            return null;
        }
    }

    private ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getImages() != null && !product.getImages().isEmpty() ? product.getImages().getFirst() : null,
                product.getPrice().intValue(),
                null,
                product.getDescription(),
                null
        );
    }
}