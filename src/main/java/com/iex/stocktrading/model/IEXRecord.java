package com.iex.stocktrading.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IEXRecord {

    private String symbol;
    private String companyName;
    private String primaryExchange;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double latestPrice;
    private String latestTime;
    private Long latestVolume;
    private Long marketCap;
}
