package kitchenpos.product.dto;

import java.math.BigDecimal;

public class ProductRequestDto {

    private String name;
    private BigDecimal price;

    public ProductRequestDto() {
    }

    public ProductRequestDto(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}