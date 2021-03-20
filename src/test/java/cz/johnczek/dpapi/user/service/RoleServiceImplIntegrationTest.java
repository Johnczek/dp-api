package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.user.entity.RoleEntity;
import cz.johnczek.dpapi.user.enums.RoleEnum;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RoleServiceImplIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RoleServiceImpl instance;

    @Nested
    class FindByCode {

        @Test
        void notExistingCode_emptyResult() {

            Optional<RoleEntity> resultOpt = instance.findByCode(RoleEnum.NOT_EXISTING_TESTING_ROLE);

            assertThat(resultOpt).isEmpty();
        }

        @Test
        void existingCode_resultReturned() {

            Optional<RoleEntity> resultOpt = instance.findByCode(RoleEnum.USER);

            assertThat(resultOpt).isNotEmpty();
            RoleEntity result = resultOpt.get();

            assertThat(result.getCode()).isEqualTo(RoleEnum.USER);
        }
    }
}