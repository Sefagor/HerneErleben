package de.fh.dortmund.eventApp.dto;

import lombok.Getter;

@Getter
public class LocationDTO {
    private final String city;

    private final String street;


    private final int houseNumber;

    private final int zip;

    private LocationDTO(Builder builder) {
        this.city = builder.city;
        this.street = builder.street;
        this.houseNumber = builder.houseNumber;
        this.zip = builder.zip;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private String city;

        private String street;


        private int houseNumber;

        private int zip;

        private Builder() {

        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder houseNumber(int houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder zip(int zip) {
            this.zip = zip;
            return this;
        }

        public LocationDTO build() {
            return new LocationDTO(this);
        }
    }
}
