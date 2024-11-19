package com.featherbook.featherbookbackend.usermanagement.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Request payload for updating user fields")
public class UserUpdateRequest {

    @Schema(description = "Subscription level of the user", example = "LEVEL_1")
    private String subscriptionLevel;

    @Schema(description = "List of books in the user's history")
    private List<BookHistoryRequest> booksHistory;

    @Schema(description = "List of books uploaded by the user")
    private List<UploadedBookRequest> uploadedBooks;

    @Schema(description = "List of saved book IDs", example = "[\"7fb163cc-ebf3-47b5-a506-f8262af412d2\"]")
    private List<String> savedBooks;

    @Data
    @Schema(description = "Details of a book in the user's history")
    public static class BookHistoryRequest {
        @Schema(description = "ID of the book", example = "7fb163cc-ebf3-47b5-a506-f8262af412d2")
        private String idBook;
    }

    @Data
    @Schema(description = "Details of a book uploaded by the user")
    public static class UploadedBookRequest {
        @Schema(description = "ID of the book", example = "7fb163cc-ebf3-47b5-a506-f8262af412d2")
        private String idBook;
    }
}
