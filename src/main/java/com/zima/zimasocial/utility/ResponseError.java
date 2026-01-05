package com.zima.zimasocial.utility;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseError {
    private String errorCode;
    private String message;
    private long timeStamp;
}
