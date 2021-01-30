package cz.johnczek.dpapi.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto {

    private String street;

    private String streetNumber;

    private String zipCode;

    private String city;
}
