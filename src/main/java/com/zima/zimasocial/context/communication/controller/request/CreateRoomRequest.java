package com.zima.zimasocial.context.communication.controller.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRoomRequest {
    private Long participant1;
    private Long participant2;
}
