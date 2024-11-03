package com.zimaberlin.zimasocial.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenResponse {
    private String token;
    private LocalDateTime expireDate;
    private TokenResponse refreshToken;
}
