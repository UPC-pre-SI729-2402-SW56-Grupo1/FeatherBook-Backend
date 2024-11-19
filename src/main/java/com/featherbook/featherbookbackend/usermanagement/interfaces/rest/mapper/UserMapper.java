package com.featherbook.featherbookbackend.usermanagement.interfaces.rest.mapper;

import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.User;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.dto.BookHistoryResponse;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.dto.UploadedBookResponse;
import com.featherbook.featherbookbackend.usermanagement.interfaces.rest.dto.UserResponse;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class UserMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setSubscriptionLevel(user.getSubscriptionLevel().name());
        response.setSavedBooks(user.getSavedBooks());

        response.setBookHistory(user.getBookHistory().stream()
                .map(history -> {
                    BookHistoryResponse historyResponse = new BookHistoryResponse();
                    historyResponse.setIdBook(history.getBookId());
                    historyResponse.setLastTimeRead(history.getLastTimeRead().format(DATE_FORMATTER));
                    return historyResponse;
                })
                .collect(Collectors.toList()));

        response.setUploadedBooks(user.getUploadedBooks().stream()
                .map(uploaded -> {
                    UploadedBookResponse uploadedResponse = new UploadedBookResponse();
                    uploadedResponse.setIdBook(uploaded.getBookId());
                    uploadedResponse.setUploadedDate(uploaded.getUploadedDate().format(DATE_FORMATTER));
                    return uploadedResponse;
                })
                .collect(Collectors.toList()));

        return response;
    }

}
