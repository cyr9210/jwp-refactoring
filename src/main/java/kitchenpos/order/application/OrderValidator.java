package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toMap;

@Component
public class OrderValidator {

    private MenuService menuService;

    public OrderValidator(final MenuService menuService) {
        this.menuService = menuService;
    }

    public void validate(Order order) {
        validate(order, getMenus(order));
    }

    private void validate(Order order, Map<Long, Menu> menus) {

        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        for(OrderLineItem item : orderLineItems) {
            validateOrderLineItem(item, menus.get(item.getMenuId()));
        }
    }

    private void validateOrderLineItem(OrderLineItem item, Menu menu) {
        final int correctStandard = 0;
        if(item.getMenuId().equals(menu.getId()) && !item.getMenuName().equals(menu.getName())) {
            throw new IllegalArgumentException("주문항목과 메뉴항목의 이름이 일치하지 않습니다.");
        }

        if(item.getMenuId().equals(menu.getId()) && item.getMenuPrice().compareTo(menu.getMenuPrice()) != correctStandard) {
            throw new IllegalArgumentException("주문항목과 메뉴항목의 가격이 일치하지 않습니다.");
        }
    }

    private Map<Long, Menu> getMenus(Order order) {
        return menuService.findAllByIds(
                order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(toList()))
                .stream()
                .collect(toMap(Menu::getId, identity()));
    }
}