package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.core.errorhandling.exception.BankAccountNotFoundRestException;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.entity.BankAccountEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.mapper.BankAccountMapper;
import cz.johnczek.dpapi.user.repository.BankAccountRepository;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountMapper bankAccountMapper;

    private final BankAccountRepository bankAccountRepository;

    @Override
    @Transactional
    public void addBankAccount(@NonNull UserEntity user, @NonNull BankAccountCreationRequest request) {

        BankAccountEntity bankAccount = bankAccountMapper.requestToEntity(request);
        bankAccount.setUser(user);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    @Transactional
    public void deleteBankAccount(long bankAccountId, long userId) {

        BankAccountEntity bankAccount = bankAccountRepository.findByIdAndUserId(bankAccountId, userId).orElseThrow(() -> {
            log.warn("Bank account deletion failed. Bank account with id {} for user with id {} not found in database", bankAccountId, userId);

            return new BankAccountNotFoundRestException(bankAccountId);
        });

        bankAccountRepository.delete(bankAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccountDto> findByUserId(long userId) {
        return  bankAccountRepository.findByUserId(userId).stream()
                .map(bankAccountMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
