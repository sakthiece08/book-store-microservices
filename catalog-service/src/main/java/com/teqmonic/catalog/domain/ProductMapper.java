package com.teqmonic.catalog.domain;

class ProductMapper {

    static Product mapToProduct(ProductEntity productEntity) {
        return Product.builder()
                .code(productEntity.getCode())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .imageUrl(productEntity.getImageUrl())
                .price(productEntity.getPrice())
                .build();
    }
}
