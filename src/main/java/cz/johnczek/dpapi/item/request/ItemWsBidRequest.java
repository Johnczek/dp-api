package cz.johnczek.dpapi.item.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ItemWsBidRequest {

    @NotNull
    private Long userId;

    @NotNull
    private BigDecimal amount;
}
