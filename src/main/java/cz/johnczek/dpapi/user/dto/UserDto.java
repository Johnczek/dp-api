package cz.johnczek.dpapi.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String description;

    private String avatarUUID;

    private List<BankAccountDto> bankAccounts = new ArrayList<>();

    private List<AddressDto> addresses = new ArrayList<>();
}
