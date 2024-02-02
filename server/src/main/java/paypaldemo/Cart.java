package paypaldemo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Cart {
    private   List<CartItem> cartItems;
  private Double total;
}
