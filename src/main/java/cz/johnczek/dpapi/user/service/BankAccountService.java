package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import lombok.NonNull;

import java.util.List;

public interface BankAccountService {

    void addBankAccount(@NonNull UserEntity user, @NonNull BankAccountCreationRequest request);

    void deleteBankAccount(long bankAccountId, long userId);

    List<BankAccountDto> findByUserId(long userId);
}
