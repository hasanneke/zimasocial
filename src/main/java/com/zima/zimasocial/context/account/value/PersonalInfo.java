package com.zima.zimasocial.context.account.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PersonalInfo {
    private final String name;
    private final String surname;
}
