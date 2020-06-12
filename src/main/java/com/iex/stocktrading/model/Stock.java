package com.iex.stocktrading.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stocks")
public class Stock {

    @Id
    private String keyword;

    private String description = "n/a";

    public Stock(String keyword) {
        this.keyword = keyword;
    }

}
