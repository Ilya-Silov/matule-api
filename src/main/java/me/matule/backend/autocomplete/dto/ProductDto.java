package me.matule.backend.autocomplete.dto;

import java.util.Map;

public record ProductDto(
        Long itemId,
        String name,
        String pictureUrl,
        Integer price,
        String url,
        String description,
        String vendor
) {
    public static ProductDto fromApiResponse(Map<String, Object> apiResponse) {
        return new ProductDto(
                Long.valueOf(apiResponse.get("ItemId").toString()),
                (String) apiResponse.get("Name"),
                (String) apiResponse.get("PictureUrl"),
                Integer.valueOf(apiResponse.get("Price").toString()),
                (String) apiResponse.get("Url"),
                (String) apiResponse.get("Description"),
                (String) apiResponse.get("Vendor")
        );
    }
}