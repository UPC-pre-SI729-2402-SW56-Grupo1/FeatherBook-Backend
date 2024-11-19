package com.featherbook.featherbookbackend.usermanagement.interfaces.rest.dto;

import lombok.Data;

@Data
public class UploadedBookResponse {
    private String idBook;
    private String uploadedDate;
}
