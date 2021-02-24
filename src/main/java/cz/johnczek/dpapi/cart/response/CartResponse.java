package cz.johnczek.dpapi.cart.response;

import cz.johnczek.dpapi.item.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    long userId;

    List<ItemDto> cartItems;
}
