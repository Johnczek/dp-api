package cz.johnczek.dpapi.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    USER_AVATAR("user-avatar"),
    UNKNOWN("unknown"),
    DELIVERY_LOGO("delivery-logo"),
    PAYMENT_LOGO("payment-logo"),
    ITEM_PICTURE("item-picture");

    private final String folder;
}
