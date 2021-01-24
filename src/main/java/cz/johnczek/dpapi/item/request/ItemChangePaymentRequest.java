package cz.johnczek.dpapi.item.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ItemChangePaymentRequest {

    @NotNull
    private Long paymentId;
}
