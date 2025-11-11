package org.cscb525.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "building")
public class Building extends BaseEntity {
    private String address;
    private int floors;
    @Column(name = "monthly_tax_per_person")
    private BigDecimal monthlyTaxPerPerson;
    @Column(name = "monthly_tax_per_pet")
    private BigDecimal monthlyTaxPerPet;
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;
    @OneToMany(mappedBy = "building")
    private Set<Apartment> apartments;

    public Building() {
    }

    public Building(String address, int floors, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet, Employee employee, Set<Apartment> apartments) {
        this.address = address;
        this.floors = floors;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
        this.employee = employee;
        this.apartments = apartments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public BigDecimal getMonthlyTaxPerPerson() {
        return monthlyTaxPerPerson;
    }

    public void setMonthlyTaxPerPerson(BigDecimal monthlyTaxPerPerson) {
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
    }

    public BigDecimal getMonthlyTaxPerPet() {
        return monthlyTaxPerPet;
    }

    public void setMonthlyTaxPerPet(BigDecimal monthlyTaxPerPet) {
        this.monthlyTaxPerPet = monthlyTaxPerPet;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    @Override
    public String toString() {
        return "Building{" +
                "address='" + address + '\'' +
                ", floors=" + floors +
                ", monthlyTaxPerPerson=" + monthlyTaxPerPerson +
                ", monthlyTaxPerPet=" + monthlyTaxPerPet +
                ", employee=" + employee +
                ", apartments=" + apartments +
                "} " + super.toString();
    }
}
