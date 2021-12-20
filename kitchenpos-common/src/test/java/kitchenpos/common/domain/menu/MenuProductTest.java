package kitchenpos.common.domain.menu;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.fixture.MenuFixtureFactory;
import kitchenpos.common.fixture.MenuGroupFixtureFactory;
import kitchenpos.common.fixture.MenuProductFixtureFactory;
import kitchenpos.common.fixture.ProductFixtureFactory;
import kitchenpos.common.domain.menu.validator.MenuProductValidator;
import kitchenpos.common.domain.menugroup.MenuGroup;
import kitchenpos.common.domain.menugroup.MenuGroupRepository;
import kitchenpos.common.domain.product.Product;
import kitchenpos.common.domain.product.ProductRepository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class MenuProductTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductValidator menuProductValidator;

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private MenuProduct 불고기_돼지고기;
    private Menu 불고기;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기, 돼지고기, 1L);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 9_000, 고기_메뉴그룹, Arrays.asList(불고기_돼지고기));

        고기_메뉴그룹 = menuGroupRepository.save(고기_메뉴그룹);
        돼지고기 = productRepository.save(돼지고기);
        불고기 = menuRepository.save(불고기);
    }

    @DisplayName("MenuProduct 는 Menu, Product, Quantity 로 생성된다.")
    @Test
    void create1() {
        // when
        MenuProduct 불고기_돼기고기 = MenuProduct.of(0L, 불고기, 돼지고기.getId(), 1L);

        // then
        assertAll(
            () -> assertEquals(불고기_돼기고기.getMenu().getId(), 불고기.getId()),
            () -> assertEquals(불고기_돼기고기.getProductId(), 돼지고기.getId()),
            () -> assertEquals(불고기_돼기고기.getQuantity(), Quantity.from(1L))
        );
    }

    @DisplayName("MenuProduct 생성 시, Menu 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, null, 돼지고기.getId(), 1L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuProductValidator.validateMenuProduct(menuProduct))
                                            .withMessageContaining("Menu 가 존재하지 않습니다.");
    }

    @DisplayName("MenuProduct 생성 시, Product 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, 불고기, 0L, 1L);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> menuProductValidator.validateMenuProduct(menuProduct));
    }

    @DisplayName("MenuProduct 는 자신의 총 합산 금액을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"1, 9000", "2, 18000", "10, 90000",})
    void calculateTotalPrice(long quantity, long totalPrice) {
        // when
        MenuProduct 불고기_돼기고기 = MenuProduct.of(돼지고기.getId(), quantity);

        // then
        assertThat(불고기_돼기고기.calculateTotalPrice(돼지고기.getPrice())).isEqualTo(BigDecimal.valueOf(totalPrice));
    }
}