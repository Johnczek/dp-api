package cz.johnczek.dpapi.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankAccountDto {

    private long id;

    private Long prefix;

    private long number;

    private int bankCode;

    private long userId;
}
