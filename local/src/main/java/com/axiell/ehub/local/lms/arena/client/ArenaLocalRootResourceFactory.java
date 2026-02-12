package com.axiell.ehub.local.lms.arena.client;

import com.axiell.ehub.common.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.common.controller.provider.filter.AcceptLanguageClientRequestFilter;
import com.axiell.ehub.local.lms.arena.controller.IArenaLocalRootResource;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import lombok.AllArgsConstructor;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
public class ArenaLocalRootResourceFactory implements IArenaLocalRootResourceFactory {
    private final AuthInfoParamConverterProvider authInfoParamConverterProvider;
    private final JacksonJsonProvider jacksonJsonProvider;
    private final LoggingFeature loggingFeature;

    @Override
    public IArenaLocalRootResource create(final String baseUri, final Locale locale) {
        AcceptLanguageClientRequestFilter acceptLanguageClientRequestFilter = new AcceptLanguageClientRequestFilter(locale);
        final List<?> providers = Arrays.asList(acceptLanguageClientRequestFilter, jacksonJsonProvider, authInfoParamConverterProvider);
        final List<Feature> features = List.of(loggingFeature);
        return JAXRSClientFactory.create(baseUri, IArenaLocalRootResource.class, providers, features, null);
    }
}
