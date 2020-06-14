package com.iex.stocktrading.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Stock {

    @Id
    private String symbol;

    private String exchange;

    private String name;

    public Stock(String symbol) {
        this.symbol = symbol;
    }

    @JsonIgnore
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
