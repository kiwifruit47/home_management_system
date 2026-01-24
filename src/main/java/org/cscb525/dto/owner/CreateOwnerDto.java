package org.cscb525.dto.owner;

import jakarta.validation.constraints.NotBlank;

public class CreateOwnerDto {
    @NotBlank(message = "Owner name cannot be blank")
    private final String name;

    public CreateOwnerDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CreateOwnerDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
