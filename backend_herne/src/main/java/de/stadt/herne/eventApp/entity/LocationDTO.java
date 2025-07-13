package de.stadt.herne.eventApp.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Valid
public class LocationDTO {

    @NotNull
    private String city;

    @NotNull

    private String street;

    @NotNull

    private int houseNumber;

    @NotNull
    private int zip;

}



