package cz.johnczek.dpapi.item.controller;

import cz.johnczek.dpapi.item.request.ItemWsBidRequest;
import cz.johnczek.dpapi.item.response.ItemWsInfoResponse;
import cz.johnczek.dpapi.item.service.ItemService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@EnableScheduling
@RestController
@RequiredArgsConstructor
public class ItemWebSocketController {

    private final ItemService itemService;

    @MessageMapping("/ws-item/bid")
    @SendTo("/ws-item/highest-bid")
    public ResponseEntity<ItemWsInfoResponse> bid(@RequestBody @NonNull @Valid ItemWsBidRequest request) {

        LocalDateTime currentTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());

        Optional<ItemWsInfoResponse> bidOpt = Optional.empty();
        try {

             bidOpt = itemService.bid(request, currentTime);
        }
        catch (Exception e) {
            log.error("Error:", e);
        }

        return bidOpt
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
