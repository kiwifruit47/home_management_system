package org.cscb525.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {
    @NotBlank(message = "Employee name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z ]*$", message = "Employee name has to start with capital letter and consist only of letters.")
    private String name;
    @NotNull
    @ManyToOne
    private Company company;
    @OneToMany(mappedBy = "employee")
    private Set<Building> buildings = new HashSet<>();

    public Employee() {
    }

    public Employee(String name, Company company) {
        this.name = name;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
