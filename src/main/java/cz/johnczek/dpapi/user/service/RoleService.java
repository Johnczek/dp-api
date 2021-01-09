package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.entity.RoleEntity;
import lombok.NonNull;

import java.util.Optional;

public interface RoleService {

    Optional<RoleEntity> findByCode(@NonNull String code);
}
