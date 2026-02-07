package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.ErrorCauseArgumentValueTextBundle;
import com.axiell.ehub.language.Language;
import com.google.common.collect.Lists;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class ErrorCauseArgumentValueAdminController implements IErrorCauseArgumentValueAdminController {
    @Autowired
    private IErrorCauseArgumentValueTextBundleRepository textBundleRepository;
    @Autowired
    private IErrorCauseArgumentValueRepository argumentValueRepository;

    @Override
    public List<ErrorCauseArgumentType> getTypes() {
        final ErrorCauseArgumentType[] types = ErrorCauseArgumentType.values();
        final List<ErrorCauseArgumentType> typeList = Lists.newArrayList(types);
        Collections.sort(typeList, new Comparator<ErrorCauseArgumentType>() {
            @Override
            public int compare(final ErrorCauseArgumentType o1, final ErrorCauseArgumentType o2) {
                return o1.name().compareTo(o2.name());
            }
        });
        return typeList;
    }

    @Override
    @Transactional(readOnly = true)
    public ErrorCauseArgumentValue findBy(final ErrorCauseArgumentType type) {
        ErrorCauseArgumentValue argumentValue = argumentValueRepository.findOneByType(type);
        return initialize(argumentValue);
    }

    @Override
    public ErrorCauseArgumentValueTextBundle save(ErrorCauseArgumentValueTextBundle textBundle) {
        return textBundleRepository.save(textBundle);
    }

    @Override
    @Transactional(readOnly = false)
    public ErrorCauseArgumentValue save(final ErrorCauseArgumentValue argumentValue) {
        Map<Language, ErrorCauseArgumentValueTextBundle> textBundles = argumentValue.getTextBundles();

        if (textBundles != null) {
            for (ErrorCauseArgumentValueTextBundle textBundle : textBundles.values()) {
                save(textBundle);
            }
        }

        return argumentValueRepository.save(argumentValue);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteErrorCauseArgumentValueTextBundles(final Language language) {
        final List<ErrorCauseArgumentValueTextBundle> textBundles = textBundleRepository.findByLanguage(language);
        textBundleRepository.deleteAll(textBundles);
    }

    private ErrorCauseArgumentValue initialize(ErrorCauseArgumentValue argumentValue) {
        Hibernate.initialize(argumentValue);
        Hibernate.initialize(argumentValue.getTextBundles());
        return argumentValue;
    }


}
