package cz.johnczek.dpapi.item.controller;

import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.item.request.ItemChangePaymentRequest;
import cz.johnczek.dpapi.item.request.ItemChangePictureRequest;
import cz.johnczek.dpapi.item.request.ItemChangeRequest;
import cz.johnczek.dpapi.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllActive() {
        return new ResponseEntity<>(itemService.findAllActive(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable("id") long id) {

        Optional<ItemDto> itemOps = itemService.findByItemId(id);

        return itemOps
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Item update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ItemDto> editById(@PathVariable("id") long id, @Valid @RequestBody ItemChangeRequest request) {

        itemService.changeItem(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/delivery")
    @Operation(summary = "Item delivery update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> changeItemDeliveryMethod(@PathVariable("id") long id,
                                                       @Valid @RequestBody ItemChangeDeliveryRequest request) {
        itemService.changeItemDelivery(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PatchMapping("/{id}/payment")
    @Operation(summary = "Item payment update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> changeItemPaymentMethod(@PathVariable("id") long id,
                                                       @Valid @RequestBody ItemChangePaymentRequest request) {
        itemService.changeItemPaymentMethod(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PatchMapping("/{id}/picture")
    @Operation(summary = "Item picture update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> changeItemPictureMethod(@PathVariable("id") long id,
                                                              @Valid @RequestBody ItemChangePictureRequest request) {
        itemService.changePicture(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PatchMapping("/{id}/top")
    @Operation(summary = "Item topping endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> changeItemPictureMethod(@PathVariable("id") long id) {
        itemService.topItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Item cancellation endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> cancelItemMethod(@PathVariable("id") long id) {
        itemService.cancelItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
