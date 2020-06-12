package com.iex.stocktrading.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewUserDTO {

    private Long id;
    private String username;
    private String password;
    private String confirm_password;
}
