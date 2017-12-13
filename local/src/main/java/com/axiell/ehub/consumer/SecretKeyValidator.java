package com.axiell.ehub.consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class SecretKeyValidator extends AbstractValidator<String> {

    @Override
    protected void onValidate(final IValidatable<String> validatable) {
        final String secretKey = validatable.getValue();
        final String base64DecodedSecretKey;
        try {
            base64DecodedSecretKey = new String(Base64.getDecoder().decode(secretKey), StandardCharsets.UTF_8);
            final String base64EncodedSecretKey = Base64.getEncoder().encodeToString(base64DecodedSecretKey.getBytes(StandardCharsets.UTF_8));

            if (StringUtils.isBlank(secretKey) || !secretKey.equals(base64EncodedSecretKey)) {
                error(validatable, resourceKey());
            }
        } catch (IllegalArgumentException ex) {
            error(validatable, resourceKey());
        }

    }

    @Override
    protected String resourceKey() {
        return "msgInvalidSecretKey";
    }
}
