package paypaldemo;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    public PaypalOrder createPaypalOrder(Cart cart) {
        List<PaypalOrder.PurchaseUnit.Item> items = cart.getCartItems().stream()
                .map(i -> PaypalOrder.PurchaseUnit.Item.builder()
                        .name(i.getProduct().getTitle())
                        .quantity(i.getQuantity())
                        .unitAmount(PaypalOrder.PurchaseUnit.Item.UnitAmount.builder()
                                .currencyCode("USD")
                                .value(formatValueOfDouble(i.getProduct().getPrice().doubleValue()))
                                .build())
                        .build())
                .toList();
        List<PaypalOrder.PurchaseUnit> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(PaypalOrder.PurchaseUnit.builder()
                .amount(PaypalOrder.PurchaseUnit.Amount.builder()
                        .currencyCode("USD")
                        .value(formatValueOfDouble(cart.getTotal()))
                        .breakdown(PaypalOrder.PurchaseUnit.Amount.Breakdown.builder()
                                .itemTotal(PaypalOrder.PurchaseUnit.Amount.Breakdown.ItemTotal.builder()
                                        .currencyCode("USD")
                                        .value(formatValueOfDouble(cart.getTotal()))
                                        .build())
                                .build())
                        .build())
                .items(items)
                .build());
        return PaypalOrder.builder()
                .purchaseUnits(purchaseUnits)
                .intent(PaypalOrder.Intent.CAPTURE)
                .build();
    }

    private String formatValueOfDouble(Double num) {
        return String.format("%.2f", BigDecimal.valueOf(num).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }
}
