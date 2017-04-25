package com.axiell.ehub.security;

import com.axiell.ehub.patron.Patron;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AuthInfo {

    private final Long ehubConsumerId;
    private Long arenaAgencyMemberId;
    private final Patron patron;

    public AuthInfo(final Long arenaAgencyMemberId, final Long ehubConsumerId, final Patron patron) {
        this.arenaAgencyMemberId = arenaAgencyMemberId;
        this.ehubConsumerId = ehubConsumerId;
        this.patron = patron;
    }

    public Long getEhubConsumerId() {
        return ehubConsumerId;
    }

    public Long getArenaAgencyMemberId() {
        return arenaAgencyMemberId;
    }

    public Patron getPatron() {
        return patron;
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
