package cz.johnczek.dpapi.user.mapper;

import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "email")
    @Mapping(target = "authorities", source = "user", qualifiedByName = "rolesToAuthorities")
    LoggedUserDetails entityToLoggedUserDetails(UserEntity user);

    @Mapping(target = "password", ignore = true)
    UserEntity registerRequestToEntity(RegisterRequest registerRequest);

    @Named("rolesToAuthorities")
    default List<GrantedAuthority> rolesToAuthorities(UserEntity user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getCode().name()))
                .collect(Collectors.toList());
    }

    @Mapping(target = "avatarUUID", source = "avatar.fileIdentifier")
    UserDto entityToDto(UserEntity source);

    @Mapping(target = "avatarUUID", source = "source.avatar.fileIdentifier")
    @Mapping(target = "addresses", source = "addresses")
    @Mapping(target = "bankAccounts", source = "bankAccounts")
    UserDto entityToDto(UserEntity source, List<AddressDto> addresses, List<BankAccountDto> bankAccounts);
}
