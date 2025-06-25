package de.fh.dortmund.eventApp.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDTO {
    @NotNull
    private String name;
}
