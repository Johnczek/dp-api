package cz.johnczek.dpapi.delivery.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryDto {

    private String name;

    private BigDecimal price;

    private String description;

    private String logoUUID;
}
