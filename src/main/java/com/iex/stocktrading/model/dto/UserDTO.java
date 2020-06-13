package com.iex.stocktrading.model.dto;

import com.iex.stocktrading.model.Account;
import lombok.*;

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
public class UserDTO {

    private Long id;

    @NotBlank(message = "Please enter your first name")
    private String full_name;

    @Email(message = "Email value is incorrect")
    @NotBlank(message = "Please enter your email")
    private String email;

    @NotNull(message = "Please enter your age")
    @Digits(message = "Your age must be an integer value", integer = 10, fraction = 0)
    private Integer age;

    @Digits(message = "Account must consist of digits only.", integer = 11, fraction = 2)
    private String account;

    private BigDecimal balance;
}
