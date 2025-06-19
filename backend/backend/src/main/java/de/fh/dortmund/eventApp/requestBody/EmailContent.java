package de.fh.dortmund.eventApp.requestBody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Valid
public class EmailContent {

    @NotNull
    private String message;

    @NotNull
    private String subject;

    @Valid
    private boolean sendToEveryOne;

    private List<Long> userIDs = new ArrayList<>();

}
