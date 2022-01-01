package kitchenpos.moduledomain.order;

import static java.util.Arrays.asList;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isEqualsCompletion(final OrderStatus changeOrderStatus) {
        return COMPLETION.equals(changeOrderStatus);
    }

    public static void validStatusIsCookingOrMealThrow(final OrderStatus orderStatus) {
        if (asList(COOKING, MEAL).contains(orderStatus)) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사 상태입니다.");
        }
    }
}