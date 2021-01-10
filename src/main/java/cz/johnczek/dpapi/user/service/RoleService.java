package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.entity.RoleEntity;
import cz.johnczek.dpapi.user.enums.RoleEnum;
import lombok.NonNull;

import java.util.Optional;

public interface RoleService {

    Optional<RoleEntity> findByCode(@NonNull RoleEnum role);
}
