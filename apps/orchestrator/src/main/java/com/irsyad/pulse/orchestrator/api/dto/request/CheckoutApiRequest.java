package com.irsyad.pulse.orchestrator.api.dto.request;

import com.irsyad.pulse.orchestrator.domain.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutApiRequest {
    private String customerId;
    private String nik;
    private String fullName;
    private String dateOfBirth;
    private String occupation;
    private BigDecimal sumInsured;
    private PaymentInfo payment;
    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PaymentInfo {
        private PaymentMethod method;
        private String bank;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Item {
        private String productId;
        private Integer quantity;
    }
}
