package org.cscb525.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "company")
public class Company extends BaseEntity{
    private String name;
    @OneToMany(mappedBy = "company")
    private Set<Employee> employees;

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", employees=" + employees +
                "} " + super.toString();
    }
}
