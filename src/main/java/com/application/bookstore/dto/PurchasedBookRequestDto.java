package com.application.bookstore.dto;

import java.util.List;

public class PurchasedBookRequestDto {
    private int customerId;
    private List<Integer> bookIds;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Integer> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Integer> bookIds) {
        this.bookIds = bookIds;
    }
}