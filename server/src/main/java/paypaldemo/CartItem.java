package paypaldemo;

import lombok.Data;

@Data
public class CartItem {
    private Product product;
    private Integer quantity;
    private Double subTotal;
}
