package com.axiell.ehub.logging;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;

import java.util.Date;

public class ToString {

    private ToString() {
    }

    public static String toString(final Object object) {
        StringBuffer sb = new StringBuffer();
        append(sb, object);
        return sb.toString();
    }

    public static void append(final StringBuffer sb, final Object object) {
        InternalRecursiveToStringStyle toStringStyle = new InternalRecursiveToStringStyle();
        toStringStyle.append(sb, object);
    }

    private static class InternalRecursiveToStringStyle extends RecursiveToStringStyle {
        public InternalRecursiveToStringStyle() {
            this.setUseIdentityHashCode(false);
            this.setUseFieldNames(false);
            this.setUseClassName(false);
        }

        public void append(final StringBuffer buffer, final Object value) {
            if (value == null) {
                appendNullText(buffer, null);

            } else {
                appendInternal(buffer, null, value, isFullDetail(null));
            }
        }

        @Override
        protected boolean accept(Class<?> clazz) {
            if (Date.class.isAssignableFrom(clazz)) {
                return false;
            } else {
                return super.accept(clazz);
            }
        }
    }
}
