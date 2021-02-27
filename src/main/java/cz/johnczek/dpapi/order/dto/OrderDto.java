package cz.johnczek.dpapi.order.dto;

import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long orderId;

    private ItemDto item;

    private UserDto buyer;

    private UserDto seller;

    private LocalDateTime created;

}
