package org.cscb525.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owner")
public class Owner extends BaseEntity {
    @NotBlank(message = "Owner name cannot be blank")
    private String name;
    @ManyToMany
    @JoinTable(
            name = "owner_apartment",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "apartment_id")
    )
    private Set<Apartment> apartments = new HashSet<>();

    public Owner() {
    }

    public Owner(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
