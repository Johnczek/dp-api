package cz.johnczek.dpapi.item.controller;

import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Method returns all active items")
    @ApiResponse(responseCode = "200", description = "All active items are returned")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDto>> getAllActive() {
        return new ResponseEntity<>(itemService.findAllActive(), HttpStatus.OK);
    }

    @Operation(summary = "Method creates new item", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Dto holding new item is returned")
    @ApiResponse(responseCode = "400", description = "In case some data of item are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in")
    @ApiResponse(responseCode = "404", description = "In case that either one of logged user, payment method, delivery method or file was not found.")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> createItem(@Parameter(description="Request holding data for new item") @NonNull @RequestBody @Valid ItemCreationRequest request) {

        Optional<ItemDto> itemOpt = itemService.createItem(request);

        return itemOpt
                .map(i -> new ResponseEntity<>(i, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @Operation(summary = "Method returns data needed to display item creation form", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Dto holding data needed to display item create page (e.g. possible payment and delivery methods etc.) is returned")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in")
    @GetMapping(value = "/creation-options", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemCreationOptionsResponse> getCreationOptions() {
        return new ResponseEntity<>(this.itemService.getItemCreationOptions(), HttpStatus.OK);
    }

    @Operation(summary = "Method returns all seller items")
    @ApiResponse(responseCode = "200", description = "Dto holding new item is returned")
    @GetMapping(value = "/seller/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDto>> getAllBySellerId(@Parameter(description="Id of seller for which we want to get its items") @PathVariable("sellerId") long sellerId) {
        return new ResponseEntity<>(itemService.findBySellerId(sellerId), HttpStatus.OK);
    }

    @Operation(summary = "Method gets item by its id")
    @ApiResponse(responseCode = "200", description = "Dto holding desired item is returned")
    @ApiResponse(responseCode = "404", description = "In case that item was not found")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> getById(@Parameter(description="Id of item we want to find") @PathVariable("id") long id) {

        ItemDto item = itemService.findByItemId(id).orElseThrow(() -> {

            log.error("Could not retrieve item, item with id {} not found", id);

            return new ItemNotFoundRestException(id);
        });

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @Operation(summary = "Method gets item by its id with additional data needed for its update", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Dto holding desired item and data needed for item updating (e.g. possible payment and delivery methods etc.) is returned")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or does not have rights to update item")
    @ApiResponse(responseCode = "404", description = "In case that item was not found")
    @GetMapping(value = "/{id}/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemEditOptionsResponse> findByItemIdForEdit(@Parameter(description="Id of item we want to find") @PathVariable("id") long id) {

        ItemEditOptionsResponse itemEditOptions = itemService.findByItemIdForEdit(id);
        return new ResponseEntity<>(itemEditOptions, HttpStatus.OK);
    }


    @Operation(summary = "Method updates item", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Dto holding updated item is returned")
    @ApiResponse(responseCode = "400", description = "In case some data of item are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update item")
    @ApiResponse(responseCode = "404", description = "In case that item was not found")
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> editById(@Parameter(description="Id of item we want to update") @PathVariable("id") long id, @Parameter(description="Data for update") @Valid @RequestBody ItemChangeRequest request) {

        itemService.changeItem(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Method changes item delivery method", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "In case update was successful")
    @ApiResponse(responseCode = "400", description = "In case some data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update item")
    @ApiResponse(responseCode = "404", description = "In case that item or delivery method were not found")
    @PatchMapping(value = "/{id}/delivery", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> changeItemDeliveryMethod(@Parameter(description="Id of item we want to update") @PathVariable("id") long id,
                                                               @Parameter(description="Request holding data of new delivery method") @Valid @RequestBody ItemChangeDeliveryRequest request) {
        itemService.changeItemDelivery(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Operation(summary = "Method changes item payment method", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "In case update was successful")
    @ApiResponse(responseCode = "400", description = "In case some data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update item")
    @ApiResponse(responseCode = "404", description = "In case that item or payment method were not found")
    @PatchMapping(value = "/{id}/payment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> changeItemPaymentMethod(@Parameter(description="Id of item we want to update") @PathVariable("id") long id,
                                                              @Parameter(description="Request holding data of new payment method") @Valid @RequestBody ItemChangePaymentRequest request) {
        itemService.changeItemPaymentMethod(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Operation(summary = "Method changes picture of item", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "In case update was successful")
    @ApiResponse(responseCode = "400", description = "In case some data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update item")
    @ApiResponse(responseCode = "404", description = "In case that item or picture were not found")
    @PatchMapping(value = "/{id}/picture", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> changeItemPictureMethod(@Parameter(description="Id of item we want to update") @PathVariable("id") long id,
                                                              @Parameter(description="Request holding uuid of new picture") @Valid @RequestBody ItemChangePictureRequest request) {
        itemService.changePicture(id, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Operation(summary = "Method cancelles item", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "In case update was successful")
    @ApiResponse(responseCode = "400", description = "In case item is not in state from which it can be cancelled")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update item")
    @ApiResponse(responseCode = "404", description = "In case that item was not found")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> cancelItemMethod(@Parameter(description="Id of item we want to update") @PathVariable("id") long id) {
        itemService.cancelItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
