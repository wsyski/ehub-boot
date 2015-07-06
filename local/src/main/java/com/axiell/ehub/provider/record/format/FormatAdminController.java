/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.axiell.ehub.language.Language;
import com.axiell.ehub.loan.ILoanAdminController;

/**
 * Default implementation of the {@link IFormatAdminController}.
 */
public class FormatAdminController implements IFormatAdminController {

    @Autowired
    private IFormatDecorationRepository formatDecorationRepository;

    @Autowired
    private IFormatTextBundleRepository formatTextBundleRepository;
    
    @Autowired
    private ILoanAdminController loanAdminController;

    @Override
    @Transactional(readOnly = true)
    public FormatDecoration getFormatDecoration(Long formatDecorationId) {
        FormatDecoration formatDecoration = formatDecorationRepository.findOne(formatDecorationId);
        return initialize(formatDecoration);
    }

    @Override
    @Transactional(readOnly = false)
    public FormatDecoration save(FormatDecoration formatDecoration) {
        return formatDecorationRepository.save(formatDecoration);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void delete(final FormatDecoration formatDecoration) {
        final FormatDecoration initalizedDecoration = getFormatDecoration(formatDecoration.getId());
        final Map<Language, FormatTextBundle> textBundleMap = initalizedDecoration.getTextBundles();
        if (textBundleMap != null) {
            final Collection<FormatTextBundle> textBundles = textBundleMap.values();
            formatTextBundleRepository.delete(textBundles);
        }
        formatDecorationRepository.delete(initalizedDecoration);
    }

    @Override
    @Transactional(readOnly = false)
    public FormatTextBundle save(FormatTextBundle formatTextBundle) {
        return formatTextBundleRepository.save(formatTextBundle);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deletable(final FormatDecoration formatDecoration) {
	long count = loanAdminController.countLoansByFormatDecoration(formatDecoration);
        return count == 0;
    }
    
    @Override
    @Transactional(readOnly = false)
    public void delete(FormatTextBundle formatTextBundle) {
        formatTextBundleRepository.delete(formatTextBundle);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteFormatTextBundles(Language language) {
        final List<FormatTextBundle> textBundles = formatTextBundleRepository.findByLanguage(language);
        formatTextBundleRepository.delete(textBundles);
    }

    /**
     * Initializes the {@link FormatDecoration}, which includes initializing all its {@link FormatTextBundle}s.
     * 
     * @param formatDecoration the {@link FormatDecoration} to initialize
     * @return a completely initialized {@link FormatDecoration}
     */
    private FormatDecoration initialize(FormatDecoration formatDecoration) {
        Hibernate.initialize(formatDecoration.getTextBundles());
        return formatDecoration;
    }
}
