package cz.johnczek.dpapi.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDto {

    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private String logoUUID;
}
