package com.iex.stocktrading.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stocks")
public class Stock {

    @Id
    private String symbol;

    private String exchange;

    private String name;

    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY)
    private Set<UserStock> stocks;

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", exchange='" + exchange + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
