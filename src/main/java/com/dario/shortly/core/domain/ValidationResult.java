package com.dario.shortly.core.domain;

public record ValidationResult(
        boolean isValid,
        String message
) {
}
