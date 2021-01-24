package cz.johnczek.dpapi.item.controller;

import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    ResponseEntity<List<ItemDto>> getAll() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ItemDto> getById(@PathVariable("id") long id) {

        Optional<ItemDto> itemOps = itemService.findByItemId(id);

        return itemOps
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PatchMapping("/{id}/delivery")
    @Operation(summary = "Item delivery update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<HttpStatus> changeItemPaymentMethod(@PathVariable("id") long id,
                                                       @RequestBody ItemChangeDeliveryRequest request) {
        itemService.changeItemDelivery(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
