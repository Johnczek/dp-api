package cz.johnczek.dpapi.item.response;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreationOptionsResponse {

    private Collection<DeliveryDto> deliveries;

    private Collection<PaymentDto> payments;
}
