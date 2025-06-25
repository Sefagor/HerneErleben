package de.stadt.herne.eventApp.entity;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDTO {
    @NotNull
    private String name;
}
