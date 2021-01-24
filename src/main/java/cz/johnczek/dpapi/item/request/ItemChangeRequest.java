package cz.johnczek.dpapi.item.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ItemChangeRequest {

    private String name;

    private String description;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;
}
