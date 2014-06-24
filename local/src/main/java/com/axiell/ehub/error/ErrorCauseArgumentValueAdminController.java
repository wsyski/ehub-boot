package com.axiell.ehub.error;

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

import static com.axiell.ehub.ErrorCauseArgumentValue.Type;

@Component
class ErrorCauseArgumentValueAdminController implements IErrorCauseArgumentValueAdminController {
    @Autowired
    private IErrorCauseArgumentValueTextBundleRepository textBundleRepository;
    @Autowired
    private IErrorCauseArgumentValueRepository argumentValueRepository;

    @Override
    public List<Type> getTypes() {
        final Type[] types = Type.values();
        final List<Type> typeList = Lists.newArrayList(types);
        Collections.sort(typeList, new Comparator<Type>() {
            @Override
            public int compare(final Type o1, final Type o2) {
                return o1.name().compareTo(o2.name());
            }
        });
        return typeList;
    }

    @Override
    @Transactional(readOnly = true)
    public ErrorCauseArgumentValue findBy(final Type type) {
        ErrorCauseArgumentValue argumentValue = argumentValueRepository.findByType(type);
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
        textBundleRepository.delete(textBundles);
    }

    private ErrorCauseArgumentValue initialize(ErrorCauseArgumentValue argumentValue) {
        Hibernate.initialize(argumentValue);
        Hibernate.initialize(argumentValue.getTextBundles());
        return argumentValue;
    }


}
