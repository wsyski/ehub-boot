package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.ep.ErrorDTO;
import com.axiell.ehub.provider.record.issue.Issue;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class ExceptionContentProviderDataAccessorStub implements IContentProviderDataAccessor {
    private static final ErrorDTO ERROR_DTO;

    static {
        ERROR_DTO = new ErrorDTO("code", "message");
    }

    @Override
    public List<Issue> getIssues(final CommandData commandData) {

        Response response = new Response() {
            @Override
            public int getStatus() {
                return Status.BAD_REQUEST.getStatusCode();
            }

            @Override
            public StatusType getStatusInfo() {
                return Status.fromStatusCode(Status.BAD_REQUEST.getStatusCode());
            }

            @Override
            public Object getEntity() {
                return ERROR_DTO;
            }

            @Override
            public <T> T readEntity(Class<T> entityType) {
                return null;
            }

            @Override
            public <T> T readEntity(GenericType<T> entityType) {
                return null;
            }

            @Override
            public <T> T readEntity(Class<T> entityType, Annotation[] annotations) {
                return null;
            }

            @Override
            public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) {
                return null;
            }

            @Override
            public boolean hasEntity() {
                return false;
            }

            @Override
            public boolean bufferEntity() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Set<String> getAllowedMethods() {
                return Set.of();
            }

            @Override
            public Map<String, NewCookie> getCookies() {
                return Map.of();
            }

            @Override
            public EntityTag getEntityTag() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public Date getLastModified() {
                return null;
            }

            @Override
            public URI getLocation() {
                return null;
            }

            @Override
            public Set<Link> getLinks() {
                return Set.of();
            }

            @Override
            public boolean hasLink(String relation) {
                return false;
            }

            @Override
            public Link getLink(String relation) {
                return null;
            }

            @Override
            public Link.Builder getLinkBuilder(String relation) {
                return null;
            }

            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getStringHeaders() {
                return null;
            }

            @Override
            public String getHeaderString(String name) {
                return "";
            }
        };
        throw new ClientErrorException(response);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData commandData) {
        throw new NotImplementedException("createLoan has not been implemented");
    }

    @Override
    public Content getContent(final CommandData commandData) {
        throw new NotImplementedException("getLoan has not been implemented");
    }
}
