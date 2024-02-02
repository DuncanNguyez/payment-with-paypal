package paypaldemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin(origins = "*")
public class Controller {

    @Autowired
    private PaypalService paypalService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public Mono<ResponseEntity<ApiRes>> createOrder(@RequestBody Cart cart) {
        log.info(cart.toString());
        return paypalService.createOrder(orderService.createPaypalOrder(cart))
                .map(data -> ApiRes.builder().data(data).build())
                .map(ResponseEntity::ok);
    }
    @PostMapping("/orders/{orderId}/capture")
    public Mono<ResponseEntity<ApiRes>> captureOrder(@PathVariable("orderId") String orderId){
        return paypalService.captureOrder(orderId)
                .map(data->ApiRes.builder().data(data).build())
                .map(ResponseEntity::ok);
    }
}
