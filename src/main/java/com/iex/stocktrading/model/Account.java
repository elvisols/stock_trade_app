package com.iex.stocktrading.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account extends BaseEntity {

    @Column(unique = true)
    private String no;

    private String name;

    @Column(precision = 2, columnDefinition = "decimal(11,2) default 0.00")
    private BigDecimal balance;

    @MapsId
    @OneToOne
    private User user;

    @Override
    public String toString() {
        return "Account{" +
                "id='" + getId() + '\'' +
                "no='" + no + '\'' +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
