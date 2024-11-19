package com.featherbook.featherbookbackend.usermanagement.domain.model.enums;

import lombok.Getter;

public enum SubscriptionLevel {
    LEVEL_1(0.5, false, false),
    LEVEL_2(0.75, true, false),
    LEVEL_3(1.0, true, true);

    @Getter
    private final double bookAccessPercentage;
    private final boolean canCommentAndRate;
    private final boolean canPublishBooks;

    SubscriptionLevel(double bookAccessPercentage, boolean canCommentAndRate, boolean canPublishBooks) {
        this.bookAccessPercentage = bookAccessPercentage;
        this.canCommentAndRate = canCommentAndRate;
        this.canPublishBooks = canPublishBooks;
    }

    public boolean canCommentAndRate() {
        return canCommentAndRate;
    }

    public boolean canPublishBooks() {
        return canPublishBooks;
    }
}
