package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.entity.RoleEntity;
import cz.johnczek.dpapi.user.repository.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleEntity> findByCode(@NonNull String code) {
        return roleRepository.findByCode(code);
    }
}
