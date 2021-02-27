package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.entity.RoleEntity;
import cz.johnczek.dpapi.user.enums.RoleEnum;
import lombok.NonNull;

import java.util.Optional;

public interface RoleService {

    /**
     * @param role role we want to find
     * @return corresponding role entity, empty optional in case role could not be found or retrieved
     */
    Optional<RoleEntity> findByCode(@NonNull RoleEnum role);
}
