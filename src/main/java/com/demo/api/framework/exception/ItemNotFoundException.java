package com.demo.api.framework.exception;

import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.util.XpertUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(long item) {
        super(XpertUtil.getMessage(
                "xpert.framework.itemNotFoundException", item));
    }

    public ItemNotFoundException(String item) {
        super(XpertUtil.getMessage(
                "xpert.framework.itemNotFoundException", item));
    }
}
