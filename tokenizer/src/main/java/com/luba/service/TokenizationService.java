package com.luba.service;

import javax.validation.constraints.NotNull;

public interface TokenizationService {


    @NotNull
    Integer getTokenCount(@NotNull String text);

    @NotNull
    <T> Integer getTokenCount(@NotNull T object);

    static TokenizationService getInstance() {
        return null;
    }
}
