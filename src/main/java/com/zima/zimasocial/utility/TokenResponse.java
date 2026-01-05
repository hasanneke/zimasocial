package com.zima.zimasocial.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenResponse {
    private String token;
    private OffsetDateTime expireDate;
    private TokenResponse refreshToken;
}
