package order.ui;

import common.domain.OrderStatus;
import java.net.URI;
import java.util.List;
import order.application.OrderService;
import order.dto.OrderRequestDto;
import order.dto.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponseDto> create(@RequestBody final OrderRequestDto request) {
        final OrderResponseDto created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponseDto>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponseDto> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatus orderStatus
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatus));
    }
}