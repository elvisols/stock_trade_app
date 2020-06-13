package com.iex.stocktrading.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date created;
    private Date modified;

    @PrePersist
    protected void onCreate() {
        this.created = new Date();
    }

    @PreUpdate()
    protected void onUpdate() {
        this.modified = new Date();
    }

}
