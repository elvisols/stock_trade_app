package com.iex.stocktrading.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.model.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionDTO {

    private Integer shares;
    private String activity;
    private double amount;
    private Date timestamp;
    private String user;
    private String stock;

}
