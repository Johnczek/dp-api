package cz.johnczek.dpapi.item.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ItemCreationRequest {

    @NotNull
    private String name;

    private String description;

    @FutureOrPresent
    private LocalDateTime validFrom;

    @Future
    private LocalDateTime validTo;

    @NotNull
    private String pictureUUID;

    @Min(1)
    private long startingPrice;

    @NotNull
    private long deliveryId;

    @NotNull
    private long paymentId;
}
