package org.cscb525.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class UpdateCompanyDto {
    @NotNull
    @Positive
    private long id;
    @NotBlank(message = "Company name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Company name must start with a capital letter and consist only of letters")
    private String name;

    public UpdateCompanyDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @NotNull
    @Positive
    public long getId() {
        return id;
    }

    public void setId(@NotNull @Positive long id) {
        this.id = id;
    }

    public @NotBlank(message = "Company name cannot be blank") @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Company name must start with a capital letter and consist only of letters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Company name cannot be blank") @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Company name must start with a capital letter and consist only of letters") String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UpdateCompanyDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
