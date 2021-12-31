package kitchenpos.order.acceptance.step;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.testutils.RestAssuredUtils;

public class OrderAcceptStep {

	private static final String BASE_URL = "/api/orders";

	public static void 주문_상태_변경_확인(ExtractableResponse<Response> 주문_상태_변경_응답, OrderUpdateRequest 상태_변경_요청_데이터) {
		OrderResponse 변경된_테이블 = 주문_상태_변경_응답.as(OrderResponse.class);

		assertAll(
			() -> assertThat(주문_상태_변경_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(변경된_테이블.getOrderStatus()).isEqualTo(상태_변경_요청_데이터.getOrderStatus())
		);
	}

	public static ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> 주문_생성_응답,
		OrderUpdateRequest 상태_변경_요청_데이터) {
		String url = 주문_생성_응답.header("Location") + "/order-status";
		return RestAssuredUtils.put(url, 상태_변경_요청_데이터);
	}

	public static void 주문_목록_조회_확인(ExtractableResponse<Response> 주문_목록_조회_응답, OrderResponse 생성된_주문) {
		List<OrderResponse> 조회된_주문_목록 = 주문_목록_조회_응답.as(new TypeRef<List<OrderResponse>>() {
		});

		assertAll(
			() -> assertThat(주문_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(조회된_주문_목록).satisfies(조회된_테이블_목록_확인(생성된_주문))
		);
	}

	private static Consumer<List<? extends OrderResponse>> 조회된_테이블_목록_확인(OrderResponse 생성된_주문) {
		return orders -> {
			assertThat(orders.size()).isOne();
			assertThat(orders).first()
				.satisfies(order -> {
					assertThat(order.getId()).isEqualTo(생성된_주문.getId());
					assertThat(order.getOrderTableId()).isEqualTo(생성된_주문.getOrderTableId());
					assertThat(order.getOrderStatus()).isEqualTo(생성된_주문.getOrderStatus());
				});
		};
	}

	public static ExtractableResponse<Response> 주문_목록_조회_요청() {
		return RestAssuredUtils.get(BASE_URL);
	}

	public static OrderResponse 주문_등록_확인(ExtractableResponse<Response> 주문_생성_응답, OrderRequest 등록_요청_데이터) {
		OrderResponse 생성된_주문 = 주문_생성_응답.as(OrderResponse.class);

		assertAll(
			() -> assertThat(주문_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(생성된_주문).satisfies(생성된_주문_확인(등록_요청_데이터))
		);

		return 생성된_주문;
	}

	public static Consumer<OrderResponse> 생성된_주문_확인(OrderRequest 등록_요청_데이터) {
		return order -> {
			assertThat(order.getId()).isNotNull();
			assertThat(order.getOrderTableId()).isEqualTo(등록_요청_데이터.getOrderTableId());
			assertThat(order.getOrderStatus().name()).isEqualTo(OrderStatus.COOKING.name());
			주문_항목_확인(order, 등록_요청_데이터);
		};
	}

	public static void 주문_항목_확인(OrderResponse 생성된_데이터, OrderRequest 생성_요청_데이터) {
		OrderLineItemResponse 생성한_주문_항목 = 생성된_데이터.getOrderLineItems().get(0);
		OrderLineItemRequest 요청한_주문_항목 = 생성_요청_데이터.getOrderLineItems().get(0);
		assertAll(
			() -> assertThat(생성한_주문_항목.getSeq()).isNotNull(),
			() -> assertThat(생성한_주문_항목.getOrderId()).isNotNull(),
			() -> assertThat(생성한_주문_항목.getMenuId()).isEqualTo(요청한_주문_항목.getMenuId()),
			() -> assertThat(생성한_주문_항목.getQuantity()).isEqualTo(요청한_주문_항목.getQuantity())
		);
	}

	public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest 등록_요청_데이터) {
		return RestAssuredUtils.post(BASE_URL, 등록_요청_데이터);
	}
}