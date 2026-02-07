package com.axiell.ehub.lms.arena.client;

import com.axiell.ehub.controller.provider.converter.AuthInfoParamConverterProvider;
import com.axiell.ehub.controller.provider.filter.AcceptLanguageClientRequestFilter;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.lms.arena.controller.IArenaLocalRootResource;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@AllArgsConstructor
public class ArenaLocalRootResourceFactory implements IArenaLocalRootResourceFactory {
    private final JsonProvider jsonProvider;
    private final AuthInfoParamConverterProvider authInfoParamConverterProvider;
    private final boolean verbose;
    private final boolean prettyLogging;

    @Override
    public IArenaLocalRootResource create(final String baseUri, final Locale locale) {
        AcceptLanguageClientRequestFilter acceptLanguageClientRequestFilter = new AcceptLanguageClientRequestFilter(locale);
        final List<?> providers = Arrays.asList(acceptLanguageClientRequestFilter, jsonProvider, authInfoParamConverterProvider);
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setVerbose(verbose);
        loggingFeature.setPrettyLogging(prettyLogging);
        final List<Feature> features = List.of(loggingFeature);
        return JAXRSClientFactory.create(baseUri, IArenaLocalRootResource.class, providers, features, null);
    }
}
