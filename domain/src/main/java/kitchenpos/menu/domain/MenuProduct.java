package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {
    private static final String INVALID_MENU = "메뉴 상품을 만들기 위해서는 메뉴가 존재해야 합니다.";
    private static final String INVALID_PRODUCT = "메뉴 상품을 만들기 위해서는 상품이 존재해야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long menuId, Product product, long quantity) {
        validateMenu(menuId);
        validateProduct(product);
        this.menuId = menuId;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    private void validateMenu(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException(INVALID_MENU);
        }
    }

    private void validateProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException(INVALID_PRODUCT);
        }
    }

    public Long id() {
        return id;
    }

    public Long menuId() {
        return menuId;
    }

    public BigDecimal price(long quantity) {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }

    public Product product() {
        return product;
    }

    public long quantity() {
        return quantity.quantity();
    }

}