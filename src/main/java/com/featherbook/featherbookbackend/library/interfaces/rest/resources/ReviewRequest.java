package com.featherbook.featherbookbackend.library.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request payload for adding or updating a review")
public class ReviewRequest {

    @NotBlank(message = "Comment is mandatory")
    @Schema(description = "Review comment", example = "Amazing book! Totally recommended.")
    private String comment;

    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 10, message = "Score must be at most 10")
    @Schema(description = "Review score between 1 and 10", example = "8")
    private int score;
}
