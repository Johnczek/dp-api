package cz.johnczek.dpapi.order.dto;

import cz.johnczek.dpapi.item.dto.ItemDto;
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

    Long orderId;

    ItemDto item;

    LocalDateTime created;

}