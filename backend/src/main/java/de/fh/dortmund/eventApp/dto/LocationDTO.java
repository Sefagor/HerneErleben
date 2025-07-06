package de.fh.dortmund.eventApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {
    private String city;

    private String street;


    private int houseNumber;

    private int zip;

}
