package com.iex.stocktrading.model.dto;

import com.iex.stocktrading.model.Stock;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserStockDTO {

    private Long id;

    @Valid
    UserDTO user;

    @Valid
    Stock stock;

    @Digits(message = "Stock shares must consist of digits only.", integer = 11, fraction = 2)
    Integer shares;

    @Digits(message = "Stock current price must consist of digits only.", integer = 11, fraction = 2)
    BigDecimal current_price;
}
