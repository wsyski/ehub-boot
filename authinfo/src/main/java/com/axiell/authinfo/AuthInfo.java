package com.axiell.authinfo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AuthInfo {
    private final Builder builder;

    private AuthInfo(final Builder builder) {
        this.builder = builder;
    }

    public Long getEhubConsumerId() {
        return builder.getEhubConsumerId();
    }

    public Long getArenaAgencyMemberId() {
        return builder.getArenaAgencyMemberId();
    }

    public String getSiteId() {
        return builder.getSiteId();
    }

    public Patron getPatron() {
        return builder.getPatron();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AuthInfo authInfo = (AuthInfo) o;

        return new EqualsBuilder()
                .append(builder, authInfo.builder)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(builder)
                .toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static final class Builder {
        private Long ehubConsumerId;
        private Long arenaAgencyMemberId;
        private String siteId;
        private Patron patron;

        public Long getEhubConsumerId() {
            return ehubConsumerId;
        }

        public Long getArenaAgencyMemberId() {
            return arenaAgencyMemberId;
        }

        public String getSiteId() {
            return siteId;
        }

        public Patron getPatron() {
            return patron;
        }

        public Builder ehubConsumerId(final Long ehubConsumerId) {
            this.ehubConsumerId = ehubConsumerId;
            return this;
        }

        public Builder arenaAgencyMemberId(final Long arenaAgencyMemberId) {
            this.arenaAgencyMemberId = arenaAgencyMemberId;
            return this;
        }

        public Builder siteId(final String siteId) {
            this.siteId = siteId;
            return this;
        }

        public Builder patron(final Patron patron) {
            this.patron = patron;
            return this;
        }

        public AuthInfo build() {
            return new AuthInfo(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Builder builder = (Builder) o;

            return new EqualsBuilder()
                    .append(getEhubConsumerId(), builder.getEhubConsumerId())
                    .append(getArenaAgencyMemberId(), builder.getArenaAgencyMemberId())
                    .append(getSiteId(), builder.getSiteId())
                    .append(getPatron(), builder.getPatron())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(getEhubConsumerId())
                    .append(getArenaAgencyMemberId())
                    .append(getSiteId())
                    .append(getPatron())
                    .toHashCode();
        }
    }
}
