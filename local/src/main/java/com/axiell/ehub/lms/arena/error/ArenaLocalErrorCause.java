package com.axiell.ehub.lms.arena.error;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

import jakarta.ws.rs.core.Response;
import java.util.Map;

public enum ArenaLocalErrorCause {
    AGENCY_NOT_FOUND_EXCEPTION(
            "Agency not found with id: ${agencyId} name: ${agencyName}",
            Response.Status.NOT_FOUND.getStatusCode()),
    ARENA_MEMBER_NOT_FOUND_EXCEPTION(
            "Agency member not found with id: ${arenaMemberId} name: ${arenaMemberName}",
            Response.Status.NOT_FOUND.getStatusCode()),
    GROUP_NOT_FOUND_EXCEPTION(
            "Group not found name: ${groupName} parent path: ${parentGroupPath}",
            Response.Status.NOT_FOUND.getStatusCode()),
    INSTALLATION_NOT_FOUND_EXCEPTION(
            "Installation not found with id: ${arenaInstallationId} name: ${arenaInstallationName}",
            Response.Status.NOT_FOUND.getStatusCode()),
    RECORD_NOT_FOUND_EXCEPTION(
            "Record not found with id: ${recordId} agency name: ${agencyName}",
            Response.Status.NOT_FOUND.getStatusCode()),
    BAD_REQUEST("Bad request error: ${throwableClassName}", Response.Status.BAD_REQUEST.getStatusCode()),
    INVALID_AUTHORIZATION_SCHEME_EXCEPTION(
            "Invalid Authorization scheme expected: Bearer got: ${scheme}",
            Response.Status.UNAUTHORIZED.getStatusCode()),
    INVALID_QUERY_EXCEPTION(
            "Invalid query: ${query} message: ${message}",
            Response.Status.BAD_REQUEST.getStatusCode()),
    MISSING_AUTHORIZATION_HEADER_EXCEPTION(
            "Missing Authorization Header",
            Response.Status.UNAUTHORIZED.getStatusCode()),
    PORTAL_SITE_NOT_FOUND_EXCEPTION(
            "Portal site not found with search criteria id: ${portalSiteId} name: ${portalSiteName} friendly url: ${friendlyUrl} virtual host: ${virtualHost}",
            Response.Status.NOT_FOUND.getStatusCode()),
    STORE_ALMA_EXCEPTION("LMS error: ${msgId}", Response.Status.BAD_REQUEST.getStatusCode()),
    UNAUTHORIZED("Unauthorized access", Response.Status.UNAUTHORIZED.getStatusCode()),
    UNREGISTERED_USER_EXCEPTION(
            "Unregistered as Arena user barcode: ${libraryCard}",
            Response.Status.UNAUTHORIZED.getStatusCode()),
    USER_BLOCKED_EXCEPTION("User blocked user id: ${userId} nick: ${userNick} block date: ${blockDate}", Response.Status.FORBIDDEN.getStatusCode()),
    USER_NOT_FOUND_EXCEPTION("User not found with id: ${userId} nick: ${nick} agency member id: ${arenaMemberId} barcode: $libraryCard} display name: ${displayName}",
            Response.Status.NOT_FOUND.getStatusCode());

    private final String message;
    private final int statusCode;

    ArenaLocalErrorCause(final String message, final int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage(final Map<String, String> arguments) {
        StrSubstitutor strSubstitutor = new StrSubstitutor(new StrLookup<String>() {
            @Override
            public String lookup(final String key) {
                return arguments.containsKey(key) ? arguments.get(key) : StringUtils.EMPTY;
            }
        });
        return strSubstitutor.replace(message);
    }

    public ArenaLocalRestApiError toError() {
        return new ArenaLocalRestApiError.Builder(this, message).build();
    }

    public ArenaLocalRestApiError toError(final Map<String, String> arguments) {
        if (arguments == null) {
            return toError();
        } else {
            return new ArenaLocalRestApiError.Builder(this, getMessage(arguments)).arguments(arguments).build();
        }
    }


}
