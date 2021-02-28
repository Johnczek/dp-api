package cz.johnczek.dpapi.item.controller;

import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.item.request.ItemChangePaymentRequest;
import cz.johnczek.dpapi.item.request.ItemChangePictureRequest;
import cz.johnczek.dpapi.item.request.ItemChangeRequest;
import cz.johnczek.dpapi.item.request.ItemCreationRequest;
import cz.johnczek.dpapi.item.response.ItemCreationOptionsResponse;
import cz.johnczek.dpapi.item.response.ItemEditOptionsResponse;
import cz.johnczek.dpapi.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDto>> getAllActive() {
        return new ResponseEntity<>(itemService.findAllActive(), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> createItem(@NonNull @RequestBody @Valid ItemCreationRequest request) {

        Optional<ItemDto> itemOpt = itemService.createItem(request);

        return itemOpt
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @GetMapping(value = "/creation-options", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemCreationOptionsResponse> getCreationOptions() {
        return new ResponseEntity<>(this.itemService.getItemCreationOptions(), HttpStatus.OK);
    }

    @GetMapping(value = "/seller/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDto>> getAllBySellerId(@PathVariable("sellerId") long sellerId) {
        return new ResponseEntity<>(itemService.findBySellerId(sellerId), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> getById(@PathVariable("id") long id) {

        Optional<ItemDto> itemOpt = itemService.findByItemId(id);

        return itemOpt
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/{id}/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemEditOptionsResponse> findByItemIdForEdit(@PathVariable("id") long id) {

        ItemEditOptionsResponse itemEditOptions = itemService.findByItemIdForEdit(id);
        return new ResponseEntity<>(itemEditOptions, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Item update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ItemDto> editById(@PathVariable("id") long id, @Valid @RequestBody ItemChangeRequest request) {

        itemService.changeItem(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/delivery", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Item delivery update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> changeItemDeliveryMethod(@PathVariable("id") long id,
                                                       @Valid @RequestBody ItemChangeDeliveryRequest request) {
        itemService.changeItemDelivery(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PatchMapping(value = "/{id}/payment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Item payment update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> changeItemPaymentMethod(@PathVariable("id") long id,
                                                       @Valid @RequestBody ItemChangePaymentRequest request) {
        itemService.changeItemPaymentMethod(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PatchMapping(value = "/{id}/picture", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Item picture update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> changeItemPictureMethod(@PathVariable("id") long id,
                                                              @Valid @RequestBody ItemChangePictureRequest request) {
        itemService.changePicture(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PatchMapping(value = "/{id}/top", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Item topping endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> topItemMethod(@PathVariable("id") long id) {
        itemService.topItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Item cancellation endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> cancelItemMethod(@PathVariable("id") long id) {
        itemService.cancelItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
