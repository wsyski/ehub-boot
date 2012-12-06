/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.user;

/**
 * Represents that status of a login attempt.
 */
public enum LoginStatus {
    SUCCESS, USER_NOT_FOUND, INVALID_PASSWORD;
}
