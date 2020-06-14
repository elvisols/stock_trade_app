package com.iex.stocktrading.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer shares;
    @Enumerated(value = EnumType.STRING)
    private EActivity activity;
    @Column(precision = 2, columnDefinition = "decimal(11,2) default 0.00")
    private BigDecimal amount;
    private Date timestamp;
    @JsonProperty( value = "user")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne
    private Stock stock;

    @PrePersist
    protected void onCreate() {
        this.timestamp = new Date();
    }
}
