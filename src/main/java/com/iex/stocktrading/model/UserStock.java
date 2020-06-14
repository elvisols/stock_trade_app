package com.iex.stocktrading.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "user_stocks", uniqueConstraints = { @UniqueConstraint( columnNames = { "user", "stock" } ) })
public class UserStock extends BaseEntity {

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user")
    User user;

    @ManyToOne
    @JoinColumn(name = "stock")
    Stock stock;

    Integer shares;

    BigDecimal currentPrice;

    @Override
    public String toString() {
        return "UserStock{" +
                "shares=" + shares +
                ", currentPrice=" + currentPrice +
                '}';
    }
}
