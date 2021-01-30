package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import lombok.NonNull;

public interface BankAccountService {

    void addBankAccount(@NonNull UserEntity user, @NonNull BankAccountCreationRequest request);

    void deleteBankAccount(long bankAccountId, long userId);
}
