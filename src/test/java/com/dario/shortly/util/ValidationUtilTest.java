package com.dario.shortly.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.dario.shortly.util.ValidationUtil.validateLink;
import static org.assertj.core.api.Assertions.assertThat;

public class ValidationUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://en.wikipedia.org",
            "https://en.wikipedia.org/wiki/Main_Page",
            "http://example.com:8080/path/to/resource",
            "https://sub.domain.example.co.uk/path/to/page",
            "en.wikipedia.org",
            "google.com"
    })
    void validateLink_whenLinkIsValid_shouldReturnTrueValidationResult(String link) {
        var validationResult = validateLink(link);

        assertThat(validationResult.isValid()).isTrue();
        assertThat(validationResult.message()).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "     ",
            "https://-invalid-domain.org",
            "http://invalid_domain.com",
            "https://example.com:999999/path",
            "https://example.com/invalid path",
            "aaaaaa"
    })
    void validateLink_whenLinkIsNotValid_shouldReturnFalseValidationResult(String link) {
        var validationResult = validateLink(link);

        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.message()).isNotEmpty();
    }
}
