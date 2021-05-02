package cz.johnczek.dpapi.item.controller;

import com.google.common.collect.Lists;
import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemIsNotActiveException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.NotEnoughAmountBidException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserAlreadyHasHighestBidException;
import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.enums.WsBidState;
import cz.johnczek.dpapi.item.request.ItemWsBidRequest;
import cz.johnczek.dpapi.item.response.ItemWsInfoResponse;
import cz.johnczek.dpapi.item.service.ItemService;
import cz.johnczek.dpapi.user.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@EnableScheduling
@RestController
@RequiredArgsConstructor
public class ItemWebSocketController {

    private final ItemService itemService;

    private final UserService userService;

    private final SimpMessagingTemplate template;

    @MessageMapping("/ws-item/bid")
    @SendTo("/ws-item/highest-bid")
    public ResponseEntity<ItemWsInfoResponse> bid(@RequestBody @NonNull @Valid ItemWsBidRequest request) {

        LocalDateTime currentTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());

        ItemWsInfoResponse bidInfo;
        try {
             bidInfo = itemService.bid(request, currentTime);
        } catch (BaseForbiddenRestException e) {
            log.warn("Bid failed ", e);
            return new ResponseEntity<>(
                    getErrorItemWsResponse(request, "Přihoz selhal. Nemáte právo přihazovat."),
                    HttpStatus.FORBIDDEN);
        } catch (ItemNotFoundRestException e) {
            log.warn("Bid failed ", e);
            return new ResponseEntity<>(
                    getErrorItemWsResponse(request, "Přihoz selhal. Zadaná nabídka neexistuje."),
                    HttpStatus.NOT_FOUND);
        } catch (UserAlreadyHasHighestBidException e) {
            log.warn("Bid failed ", e);
            return new ResponseEntity<>(
                    getErrorItemWsResponse(request, "Přihoz selhal. Již máte nejvyšší příhoz na této nabídce."),
                    HttpStatus.BAD_REQUEST);
        } catch (NotEnoughAmountBidException e) {
            log.warn("Bid failed ", e);
            return new ResponseEntity<>(
                    getErrorItemWsResponse(request, "Přihoz selhal. Přihazovaná částka nestačí."),
                    HttpStatus.BAD_REQUEST);
        } catch (ItemIsNotActiveException e) {
            log.warn("Bid failed ", e);
            return new ResponseEntity<>(
                    getErrorItemWsResponse(request, "Přihoz selhal. Nabídka již není aktivní."),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.warn("Bid failed ", e);
            return new ResponseEntity<>(
                    getErrorItemWsResponse(request),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(bidInfo, HttpStatus.OK);
    }

    private ItemWsInfoResponse getErrorItemWsResponse(@NonNull ItemWsBidRequest request) {

        return getErrorItemWsResponse(request, "Nebylo možné provést příhoz.");

    }

    private ItemWsInfoResponse getErrorItemWsResponse(@NonNull ItemWsBidRequest request, @NonNull String errorMessage) {

        return ItemWsInfoResponse.builder()
                .itemId(request.getItemId())
                .message(errorMessage)
                .state(WsBidState.ERROR)
                .userRequestId(userService.findUserByJwtToken(
                        request.getUserJwtToken())
                        .map(AbstractIdBasedEntity::getId).orElse(null))
                .build();

    }

    @Scheduled(fixedRate = 1000)
    public void checkItemStateAndSendUpdate() {
        log.debug("Expiration job: started");
        List<ItemDto> items = itemService.checkAndProcessItemsExpiration();
        if (CollectionUtils.isEmpty(items)) {
            log.debug("Expiration job: ended, no items to process found");
            return;
        }

        log.debug("Expiration job: processing items with ids {}", Lists.transform(items, ItemDto::getId));

        items.forEach((ItemDto item) -> {
            ItemWsInfoResponse response = ItemWsInfoResponse.builder()
                    .itemId(item.getId())
                    .itemState(item.getState())
                    .state(WsBidState.SUCCESS)
                    .build();

            this.template.convertAndSend("/ws-item/highest-bid", new ResponseEntity<>(response, HttpStatus.OK));
        });

        log.debug("Expiration job: ended");
    }
}
