package org.cscb525.dto.building;

import jakarta.validation.constraints.NotNull;

public class BuildingDto {
    @NotNull
    private final String address;

    public BuildingDto(String address) {
        this.address = address;
    }

    public @NotNull String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "BuildingDto{" +
                "address='" + address + '\'' +
                '}';
    }
}
