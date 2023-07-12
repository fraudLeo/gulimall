package com.leo.gulimall.gulimall.gateway;

import lombok.Getter;

@Getter
enum Binary {
    ZERO_0(0),
    SUCCESS_200(200);

    final int value;
    Binary (int value) {
        this.value = value;
    }

}
