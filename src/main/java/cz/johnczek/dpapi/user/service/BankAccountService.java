package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.core.errorhandling.exception.BankAccountNotFoundRestException;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BankAccountService {

    /**
     *
     * @param user user for which we want to add bank account
     * @param request request holding new bank account data
     * @return new bank account data, empty optional in case we could not add new bank account
     */
    Optional<BankAccountDto> addBankAccount(@NonNull UserEntity user, @NonNull BankAccountCreationRequest request);

    /**
     * @param bankAccountId id of bank account we want to delete
     * @param userId if of user who owns bank account
     * @throws BankAccountNotFoundRestException in case bank account could not be found
     */
    void deleteBankAccount(long bankAccountId, long userId);

    /**
     * @param userId id of user we want to retrieve its bank accounts
     * @return list of bank accounts for given user
     */
    List<BankAccountDto> findByUserId(long userId);

    /**
     * @param userIds ids of users we want to find their bank accounts
     * @return map where key is user id and value are corresponding bank accounts
     */
    Map<Long, List<BankAccountDto>> findByUserIds(@NonNull List<Long> userIds);
}
