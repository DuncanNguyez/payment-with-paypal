package paypaldemo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
// view schema at https://developer.paypal.com/docs/api/orders/v2/#orders_create
public class PaypalOrder {

    private List<PurchaseUnit> purchaseUnits;
    private Intent intent;

    @Builder
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PurchaseUnit {
        private List<Item> items;
        private Amount amount;

        @Builder
        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Item {
            private String name;
            private Integer quantity;
            private String description;
            private UnitAmount unitAmount;
            @Builder
            @Data
            public static class UnitAmount {

                // view detail at https://developer.paypal.com/api/rest/reference/currency-codes/
                String currencyCode;
                String value;
            }
        }



        @Builder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Data
        public static class Amount {

            // view detail at https://developer.paypal.com/api/rest/reference/currency-codes/
            String currencyCode ;
            String value ;
            Breakdown breakdown;
            @Builder
            @Data
            static class Breakdown {
                ItemTotal itemTotal;
                @Builder
                @Data
                static class ItemTotal{
                    // view detail at https://developer.paypal.com/api/rest/reference/currency-codes/
                    String currencyCode;
                    String value;
                }
            }
        }
    }
    public enum Intent {
        CAPTURE,
        AUTHORIZE;
    }
}

