package com.campusgram.user.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendNotiMessage {
    private String targetId;
    private String message;
}
