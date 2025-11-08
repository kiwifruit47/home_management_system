package org.cscb525.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "building")
public class Building extends BaseEntity {
    private String address;
    private int floors;
    @Column(name = "monthly_tax_value")
    private BigDecimal monthlyTaxValue;
    @ManyToOne
    private Employee employee;
    @OneToMany(mappedBy = "building")
    private Set<Apartment> apartments;
}
