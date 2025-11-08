package org.cscb525.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "apartment")
public class Apartment extends BaseEntity {
    private int floor;
    private int apartment_number;
    private BigDecimal area;
    private int pets;
    @ManyToOne
    private Building building;
    @ManyToMany(mappedBy = "apartments")
    private Set<Owner> owners;
    @OneToMany(mappedBy = "apartment")
    private Set<Occupant> occupants;
}
