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

}
