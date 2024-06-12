package com.dario.shortly.util;

import com.dario.shortly.core.domain.ValidationResult;
import lombok.experimental.UtilityClass;

import static org.springframework.util.StringUtils.hasText;

@UtilityClass
public class ValidationUtil {

    public static ValidationResult validateLink(String link) {
        if (!hasText(link)) {
            return new ValidationResult(false, "Please enter a link");
        }
        if (!link.matches("^(https?://)?(?!-)[A-Za-z0-9-]{1,63}(?<!-)(\\.(?!-)[A-Za-z0-9-]{1,63}(?<!-))*\\.[A-Za-z]{2,}(:\\d{1,5})?(/[^\\s]*)?$")) {
            return new ValidationResult(false, "Please enter a valid URL");
        }

        return new ValidationResult(true, "");
    }
}
