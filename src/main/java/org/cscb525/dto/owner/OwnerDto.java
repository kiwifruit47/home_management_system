package org.cscb525.dto.owner;

import jakarta.validation.constraints.NotBlank;

public class OwnerDto {
    @NotBlank(message = "Owner name cannot be blank")
    private final String name;

    public OwnerDto(String name) {
        this.name = name;
    }

    public @NotBlank(message = "Owner name cannot be blank") String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "OwnerDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
