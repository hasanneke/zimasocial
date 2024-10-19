package com.zimaberlin.zimasocial.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenResponse {
    private String token;
    private Date expireDate;
    private TokenResponse refreshToken;
}
