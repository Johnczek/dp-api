package cz.johnczek.dpapi.core.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Object represents response message for rest calling.
 */

@AllArgsConstructor
@Builder
@Getter
public class RestMessage {

    private RestMessageTypeEnum messageType;

    private String value;
}
