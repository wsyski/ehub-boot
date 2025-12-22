package com.axiell.ehub.consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecretKeyValidator implements IValidator<String> {

    @Override
    public void validate(final IValidatable<String> validatable) {
        final String secretKey = validatable.getValue();
        final String base64DecodedSecretKey;
        try {
            base64DecodedSecretKey = new String(Base64.getDecoder().decode(secretKey), StandardCharsets.UTF_8);
            final String base64EncodedSecretKey = Base64.getEncoder().encodeToString(base64DecodedSecretKey.getBytes(StandardCharsets.UTF_8));

            if (StringUtils.isBlank(secretKey) || !secretKey.equals(base64EncodedSecretKey)) {
                validatable.error(new ValidationError().addKey(resourceKey()));
            }
        } catch (IllegalArgumentException ex) {
            validatable.error(new ValidationError().addKey(resourceKey()));
        }

    }

    private String resourceKey() {
        return "msgInvalidSecretKey";
    }
}
