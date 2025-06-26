package de.fh.dortmund.eventApp.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class CategoryDTO {
    @NotNull
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CategoryDTO other = (CategoryDTO) obj;
        return (name != null ? name.equalsIgnoreCase(other.name) : other.name == null);
    }

}
