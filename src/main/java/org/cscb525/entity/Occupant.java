package org.cscb525.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "occupant")
public class Occupant extends BaseEntity {
    private int age;
    @Column(name = "uses_elevator")
    private boolean usesElevator;
    @ManyToOne
    private Apartment apartment;
}
