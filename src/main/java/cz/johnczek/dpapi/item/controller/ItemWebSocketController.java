package cz.johnczek.dpapi.item.controller;

import cz.johnczek.dpapi.item.request.ItemWsBidRequest;
import cz.johnczek.dpapi.item.response.ItemWsInfoResponse;
import cz.johnczek.dpapi.item.service.ItemService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@EnableScheduling
@RestController
@RequiredArgsConstructor
public class ItemWebSocketController {

    private final SimpMessagingTemplate template;

    private final ItemService itemService;

    @MessageMapping("/ws-item-in/bid/{itemId}")
    @SendTo("/ws-item-out/highest-bid/{itemId}")
    public ResponseEntity<ItemWsInfoResponse> bid(@DestinationVariable("itemId") long itemId,
                                                                  @RequestBody @NonNull @Valid ItemWsBidRequest request) {

        LocalDateTime currentTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());

        Optional<ItemWsInfoResponse> bidOpt = itemService.bid(itemId, request, currentTime);

        return bidOpt
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
