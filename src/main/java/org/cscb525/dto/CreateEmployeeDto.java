package org.cscb525.dto;

public class CreateEmployeeDto {
    private String name;
    private long companyId;

    public CreateEmployeeDto(String name, long companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "CreateEmployeeDto{" +
                "name='" + name + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
