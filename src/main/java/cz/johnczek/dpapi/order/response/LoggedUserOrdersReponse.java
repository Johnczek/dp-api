package cz.johnczek.dpapi.order.response;

import cz.johnczek.dpapi.order.dto.OrderDto;
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
public class LoggedUserOrdersReponse {

    List<OrderDto> orders;
}
