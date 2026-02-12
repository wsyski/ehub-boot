/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.loan;

import com.axiell.ehub.common.AbstractTimestampAwarePersistable;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.util.HashCodeBuilderFactory;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Represents an eHUB loan, which basically connects an LMS loan, a Content Provider loan and the eHUB consumer who
 * executed the loans.
 */
@Entity
@Table(name = "EHUB_LOAN", uniqueConstraints = @UniqueConstraint(columnNames = {"LMS_LOAN_ID", "EHUB_CONSUMER_ID"}))
@Access(AccessType.PROPERTY)
public class EhubLoan extends AbstractTimestampAwarePersistable<Long> {
    private EhubConsumer ehubConsumer;
    private LmsLoan lmsLoan;
    private ContentProviderLoanMetadata contentProviderLoanMetadata;

    /**
     * Empty constructor required by JPA.
     */
    protected EhubLoan() {
    }

    /**
     * Constructs a new {@link EhubLoan}.
     *
     * @param ehubConsumer                the {@link EhubConsumer} who executed the loans
     * @param lmsLoan                     the {@link LmsLoan}
     * @param contentProviderLoanMetadata the metadata of the {@link ContentProviderLoan}
     */
    public EhubLoan(final EhubConsumer ehubConsumer, final LmsLoan lmsLoan, final ContentProviderLoanMetadata contentProviderLoanMetadata) {
        this.ehubConsumer = ehubConsumer;
        this.lmsLoan = lmsLoan;
        this.contentProviderLoanMetadata = contentProviderLoanMetadata;
    }

    /**
     * Returns the {@link EhubConsumer} who executed the loans.
     *
     * @return an {@link EhubConsumer}
     */
    @ManyToOne
    @JoinColumn(name = "EHUB_CONSUMER_ID", nullable = false)
    public EhubConsumer getEhubConsumer() {
        return ehubConsumer;
    }

    /**
     * Sets the {@link EhubConsumer} who executed the loans.
     *
     * @param ehubConsumer the {@link EhubConsumer} who executed the loans to set
     */
    public void setEhubConsumer(final EhubConsumer ehubConsumer) {
        this.ehubConsumer = ehubConsumer;
    }

    /**
     * Returns the {@link LmsLoan}.
     *
     * @return an {@link LmsLoan}
     */
    @Embedded
    public LmsLoan getLmsLoan() {
        return lmsLoan;
    }

    /**
     * Sets the {@link LmsLoan}.
     *
     * @param lmsLoan the {@link LmsLoan} to set
     */
    public void setLmsLoan(LmsLoan lmsLoan) {
        this.lmsLoan = lmsLoan;
    }

    /**
     * Returns the {@link ContentProviderLoanMetadata}.
     *
     * @return a {@link ContentProviderLoanMetadata}
     */
    @Embedded
    public ContentProviderLoanMetadata getContentProviderLoanMetadata() {
        return contentProviderLoanMetadata;
    }

    /**
     * Sets the {@link ContentProviderLoanMetadata}.
     *
     * @param contentProviderLoanMetadata the {@link ContentProviderLoanMetadata} to set
     */
    public void setContentProviderLoanMetadata(ContentProviderLoanMetadata contentProviderLoanMetadata) {
        this.contentProviderLoanMetadata = contentProviderLoanMetadata;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EhubLoan)) {
            return false;
        }
        EhubLoan rhs = (EhubLoan) obj;
        return new EqualsBuilder().append(lmsLoan, rhs.getLmsLoan()).append(getEhubConsumer(), rhs.getEhubConsumer()).isEquals();
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return HashCodeBuilderFactory.create().append(getLmsLoan()).append(getEhubConsumer()).toHashCode();
    }
}
