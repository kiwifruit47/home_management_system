package org.cscb525.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "owner")
public class Owner extends BaseEntity {
    private String name;
    @ManyToMany
    @JoinTable(
            name = "owner_apartment",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "apartment_id")
    )
    private Set<Apartment> apartments;
}
