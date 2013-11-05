package com.axiell.ehub.util;

import java.util.Date;

public final class DateFactory {

    private DateFactory() {
    }

    public static Date create(Date date) {
	final long time = date.getTime();
	return new Date(time);
    }
}
