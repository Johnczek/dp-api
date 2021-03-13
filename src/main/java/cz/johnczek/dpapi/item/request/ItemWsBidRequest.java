package cz.johnczek.dpapi.item.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ItemWsBidRequest {

    @NotNull
    @NotBlank
    private String userJwtToken;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long itemId;
}
