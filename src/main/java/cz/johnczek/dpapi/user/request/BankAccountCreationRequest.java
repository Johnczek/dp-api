package cz.johnczek.dpapi.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class BankAccountCreationRequest {

    private Long prefix;

    @NotNull
    private Long number;

    @NotNull
    private Integer bankCode;
}
