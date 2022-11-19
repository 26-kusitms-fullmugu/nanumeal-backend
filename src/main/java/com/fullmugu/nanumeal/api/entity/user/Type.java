package com.fullmugu.nanumeal.api.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Type {
    DONATER, CHILD;

    @JsonCreator
    public static Type from(String s) {
        return Type.valueOf(s.toUpperCase());
    }
}
