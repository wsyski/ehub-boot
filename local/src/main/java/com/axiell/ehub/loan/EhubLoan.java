/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.axiell.ehub.consumer.EhubConsumer;

/**
 * Represents an eHUB loan, which basically connects an LMS loan, a Content Provider loan and the eHUB consumer who
 * executed the loans. 
 * 
 * <p>
 * An ID of the public {@link ReadyLoan} is exactly the same as the ID the underlying {@link EhubLoan}.
 * </p> 
 */
@Entity
@Table(name = "EHUB_LOAN", uniqueConstraints = @UniqueConstraint(columnNames = {"LMS_LOAN_ID", "EHUB_CONSUMER_ID"}))
@Access(AccessType.PROPERTY)
public class EhubLoan extends AbstractPersistable<Long> {
    private static final long serialVersionUID = -7812821521823344547L;
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
     * @param ehubConsumer the {@link EhubConsumer} who executed the loans
     * @param lmsLoan the {@link LmsLoan}
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
    @ForeignKey(name = "EHUB_LOAN_EHUB_CONSUMER_FK")
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
     * @see java.lang.Object#equals(java.lang.Object)
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
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getLmsLoan()).append(getEhubConsumer()).toHashCode();
    }
}
