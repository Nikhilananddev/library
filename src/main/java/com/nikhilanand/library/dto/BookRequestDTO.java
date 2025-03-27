package com.nikhilanand.library.dto;
import lombok.Getter;
import lombok.Setter;
import global.AvailabilityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
@Getter
@Setter
public class BookRequestDTO {
    @NotBlank(message = "Book ID is required")
    @Pattern(
        regexp = "^BOOK-\\d{3}$",
        message = "Book ID must follow pattern BOOK-XXX (e.g., BOOK-001)"
    )
    private String bookId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    private String genre;

    private AvailabilityStatus availabilityStatus = AvailabilityStatus.AVAILABLE;


}

