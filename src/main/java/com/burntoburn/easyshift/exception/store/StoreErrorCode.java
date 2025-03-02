package com.burntoburn.easyshift.exception.store;

import com.burntoburn.easyshift.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum StoreErrorCode implements ErrorCode {

    STORE_ACCESS_DENIED(HttpStatus.FORBIDDEN, 1001, "해당 매장에 대한 접근 권한이 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "해당 매장이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
