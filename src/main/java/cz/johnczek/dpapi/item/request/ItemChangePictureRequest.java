package cz.johnczek.dpapi.item.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ItemChangePictureRequest {

    @NotNull
    @NotBlank
    private String pictureUUID;
}
