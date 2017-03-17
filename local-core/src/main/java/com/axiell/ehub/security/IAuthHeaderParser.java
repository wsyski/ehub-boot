package com.axiell.ehub.security;

import com.axiell.ehub.patron.Patron;

public interface IAuthHeaderParser {

    Long getEhubConsumerId();

    Patron getPatron();
}
