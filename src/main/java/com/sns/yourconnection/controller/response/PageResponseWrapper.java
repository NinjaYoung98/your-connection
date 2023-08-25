package com.sns.yourconnection.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Getter
public class PageResponseWrapper {
    private int pageNumber;
    private int pageSize;
    private long totalElements;

    public static PageResponseWrapper fromPage(Page<?> page) {
        return new PageResponseWrapper(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }
}
