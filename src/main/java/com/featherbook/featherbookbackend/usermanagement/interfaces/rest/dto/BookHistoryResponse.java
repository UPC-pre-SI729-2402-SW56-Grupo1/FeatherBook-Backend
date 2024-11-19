package com.featherbook.featherbookbackend.usermanagement.interfaces.rest.dto;

import lombok.Data;

@Data
public class BookHistoryResponse {
    private String idBook;
    private String lastTimeRead;
}
