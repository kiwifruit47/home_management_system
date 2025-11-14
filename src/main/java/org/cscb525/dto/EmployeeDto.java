package org.cscb525.dto;

public class EmployeeDto {
    private String name;

    public EmployeeDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
