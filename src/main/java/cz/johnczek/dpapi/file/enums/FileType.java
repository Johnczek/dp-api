package cz.johnczek.dpapi.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    USER_AVATAR("user-avatar"),
    UNKNOWN("unknown");

    private final String folder;
}
