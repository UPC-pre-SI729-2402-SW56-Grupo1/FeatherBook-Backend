package com.featherbook.featherbookbackend.library.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Request payload for creating a new book")
public class CreateBookRequest {

    @Schema(description = "Name of the book", example = "The beginning after the end")
    private String name;

    @Schema(description = "Summary of the book", example = "Reincarnated in a new world full of magic and monsters, the king has a second chance to relive his life.")
    private String summary;

    @Schema(description = "Category of the book", example = "Fantasy")
    private String category;

    @Schema(description = "URL of the book file", example = "https://novelasligera.com/novela/la-vida-despues-de-la-muerte/")
    private String bookUrl;

}
