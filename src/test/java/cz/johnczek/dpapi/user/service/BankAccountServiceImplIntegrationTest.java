package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.core.errorhandling.exception.BankAccountNotFoundRestException;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.entity.BankAccountEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.mapper.BankAccountMapper;
import cz.johnczek.dpapi.user.repository.BankAccountRepository;
import cz.johnczek.dpapi.user.repository.UserRepository;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccountServiceImplIntegrationTest extends AbstractIntegrationTest {

    private static final int DUMMY_BANK_CODE = 1010;
    private static final long DUMMY_PREFIX = 1000;
    private static final long DUMMY_NUMBER = 123456789;

    @Autowired
    BankAccountServiceImpl instance;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    BankAccountMapper bankAccountMapper;

    @Autowired
    UserRepository userRepository;

    @Nested
    class AddBankAccount {

        @Test
        void validBankAccountRequest_bankAccountAdded() {

            UserEntity user = userRepository.findByUserId(7L).orElse(null);

            assertThat(user).isNotNull();
            assertThat(user.getBankAccounts()).isEmpty();

            BankAccountCreationRequest request = new BankAccountCreationRequest();
            request.setBankCode(DUMMY_BANK_CODE);
            request.setPrefix(DUMMY_PREFIX);
            request.setNumber(DUMMY_NUMBER);

            instance.addBankAccount(user, request);

            List<BankAccountEntity> userBankAccount = bankAccountRepository.findByUserId(7L);
            assertThat(userBankAccount).hasSize(1);

            BankAccountEntity newBankAccount = userBankAccount.get(0);
            assertAll(
                    () -> assertThat(newBankAccount.getBankCode()).isEqualTo(DUMMY_BANK_CODE),
                    () -> assertThat(newBankAccount.getNumber()).isEqualTo(DUMMY_NUMBER),
                    () -> assertThat(newBankAccount.getPrefix()).isEqualTo(DUMMY_PREFIX)
            );

            // cleanup
            bankAccountRepository.delete(newBankAccount);
            assertThat(bankAccountRepository.findByUserId(7L)).isEmpty();
        }
    }

    @Nested
    class DeleteBankAccount {

        @Test
        void validBankAccountIdAndInvalidUserId_exceptionThrown() {

            assertThrows(BankAccountNotFoundRestException.class,
                    () -> instance.deleteBankAccount(1L, 1000L)
            );
        }

        @Test
        void invalidBankAccountIdAndValidUserId_exceptionThrown() {

            assertThrows(BankAccountNotFoundRestException.class,
                    () -> instance.deleteBankAccount(1000L, 2L)
            );
        }

        @Test
        void invalidBothParams_exceptionThrown() {

            assertThrows(BankAccountNotFoundRestException.class,
                    () -> instance.deleteBankAccount(1000L, 202121L)
            );
        }

        @Test
        void validBankAccountIdAndUserId_bankAccountDeleted() {

            UserEntity user = userRepository.findByUserId(7L).orElse(null);

            assertThat(user).isNotNull();
            assertThat(user.getBankAccounts()).isEmpty();

            BankAccountEntity bankAccount = new BankAccountEntity();
            bankAccount.setBankCode(DUMMY_BANK_CODE);
            bankAccount.setPrefix(DUMMY_PREFIX);
            bankAccount.setNumber(DUMMY_NUMBER);
            bankAccount.setUser(user);
            bankAccountRepository.save(bankAccount);

            List<BankAccountEntity> userBankAccount = bankAccountRepository.findByUserId(7L);
            assertThat(userBankAccount).hasSize(1);

            instance.deleteBankAccount(bankAccount.getId(), user.getId());

            userBankAccount = bankAccountRepository.findByUserId(7L);
            assertThat(userBankAccount).isEmpty();
        }
    }

    @Nested
    class FindByUserId {

        @Test
        void nonExistingUser_emptyList() {

            List<BankAccountDto> result = instance.findByUserId(10000L);

            assertThat(result).isEmpty();
        }

        @Test
        void existingUserWithOneBankAccount_listWithOneRecord() {

            long userId = 1L;
            List<BankAccountDto> resultList = instance.findByUserId(userId);

            assertThat(resultList).isNotEmpty().hasSize(1);
            BankAccountDto firstBankAccount = resultList.get(0);

            BankAccountDto expectedResult = bankAccountMapper.entityToDto(bankAccountRepository.findByUserId(userId).get(0));

            assertThat(firstBankAccount).usingRecursiveComparison().isEqualTo(expectedResult);
        }

    }

    @Nested
    class FindByUserIds {

        @Test
        void noUser_EmptyMap() {

            Map<Long, List<BankAccountDto>> result = instance.findByUserIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        void existingUserWithOneBankAccount_mapWithOneRecord() {

            long userId = 1L;
            Map<Long, List<BankAccountDto>> result = instance.findByUserIds(Collections.singletonList(userId));

            assertThat(result).isNotEmpty().hasSize(1);
            List<BankAccountDto> userBankAccounts = result.get(userId);
            assertThat(userBankAccounts).isNotEmpty().hasSize(1);

            BankAccountDto expectedResult = bankAccountMapper.entityToDto(bankAccountRepository.findByUserId(userId).get(0));

            assertThat(userBankAccounts.get(0)).usingRecursiveComparison().isEqualTo(expectedResult);
        }

        @Test
        void userWithoutBankAccount_mapWithOneRecordHoldingEmptyList() {

            long userId = 7L;
            Map<Long, List<BankAccountDto>> result = instance.findByUserIds(Collections.singletonList(userId));

            assertThat(result).isEmpty();
        }

        @Test
        void twoUsersOneWithBankAccount_mapWithOneRecord() {

            long firstUserId = 1L;
            long secondUserId = 7L;
            Map<Long, List<BankAccountDto>> result = instance.findByUserIds(List.of(firstUserId, secondUserId));

            assertThat(result).isNotEmpty().hasSize(1);

            List<BankAccountDto> firstUserBankAccounts = result.get(firstUserId);
            assertThat(firstUserBankAccounts).isNotEmpty().hasSize(1);
            BankAccountDto expectedResult = bankAccountMapper.entityToDto(bankAccountRepository.findByUserId(firstUserId).get(0));
            assertThat(firstUserBankAccounts.get(0)).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }
}