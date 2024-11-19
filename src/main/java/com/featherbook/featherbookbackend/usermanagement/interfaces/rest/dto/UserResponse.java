package com.featherbook.featherbookbackend.usermanagement.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    @Schema(description = "Unique identifier of the user", example = "2db7b338-7870-49ff-9ae5-1c5c7b2cca79")
    private String id;

    @Schema(description = "Username of the user", example = "JaneDoe")
    private String username;

    @Schema(description = "Email of the user", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "Subscription level", example = "LEVEL_1")
    private String subscriptionLevel;

    @Schema(description = "Books history of the user")
    private List<BookHistoryResponse> bookHistory;

    @Schema(description = "Books uploaded by the user")
    private List<UploadedBookResponse> uploadedBooks;

    @Schema(description = "List of saved books", example = "[\"7fb163cc-ebf3-47b5-a506-f8262af412d2\"]")
    private List<String> savedBooks;
}
