package org.cscb525.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {
    @ManyToOne
    private Company company;
    @OneToMany(mappedBy = "employee")
    private Set<Building> buildings;
}
