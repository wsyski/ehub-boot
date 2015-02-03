/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

public class ReadyLoan {
    private Long id;
    private LmsLoan lmsLoan;
    private ContentProviderLoan contentProviderLoan;
    
    public ReadyLoan(Long id, LmsLoan lmsLoan, ContentProviderLoan contentProviderLoan) {
        this.id = id;
        this.lmsLoan = lmsLoan;
        this.contentProviderLoan = contentProviderLoan;
    }

    /**
     * Returns the ID globally unique ID of the {@link ReadyLoan}.
     *
     * @return the ID globally unique ID of the {@link ReadyLoan}
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Returns the lmsLoan.
     *
     * @return the lmsLoan
     */
    public LmsLoan getLmsLoan() {
        return lmsLoan;
    }

    /**
     * Returns the contentProviderLoan.
     *
     * @return the contentProviderLoan
     */
    public ContentProviderLoan getContentProviderLoan() {
        return contentProviderLoan;
    }
}
