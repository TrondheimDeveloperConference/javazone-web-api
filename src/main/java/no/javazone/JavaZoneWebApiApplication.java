package no.javazone;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import no.javazone.api.sessions.SessionResource;
import no.javazone.ems.EmsAdapter;
import no.javazone.helsesjekk.EmsHealthCheck;
import no.javazone.http.AddCorsHeaderToResponse;
import no.javazone.http.PathResolver;
import no.javazone.sessions.SessionRepository;
import no.javazone.sessions.SessionsCacheRefreshScheduler;
import no.javazone.speaker.SpeakerImageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import no.javazone.api.speaker.SpeakerResource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

public class JavaZoneWebApiApplication extends Application<JavaZoneWebApiConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(JavaZoneWebApiApplication.class);

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            args = new String[]{"server", "configuration.yaml"};
        }
        new JavaZoneWebApiApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<JavaZoneWebApiConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor()
                )
        );
    }

    @Override
    public void run(
            JavaZoneWebApiConfiguration configuration,
            Environment environment
    ) {
        final URI contextPath = getContextPath();
        final PathResolver pathResolver = new PathResolver(contextPath);

        final String emsHost = getEmsHost(configuration);
        LOG.info("Bruker EMS-host: " + emsHost);

        final EmsAdapter emsAdapter = new EmsAdapter(emsHost);

        SpeakerImageCache speakerImageCache = new SpeakerImageCache();
        final SessionRepository sessionRepository = new SessionRepository(emsAdapter, speakerImageCache);

        new SessionsCacheRefreshScheduler(sessionRepository).schedule();

        environment.jersey().register(new AddCorsHeaderToResponse());
        environment.jersey().register(new SessionResource(pathResolver, sessionRepository));
        environment.jersey().register(new SpeakerResource(speakerImageCache));
        environment.healthChecks().register("ems", new EmsHealthCheck(emsAdapter));
    }

    private String getEmsHost(JavaZoneWebApiConfiguration configuration) {
        Map<String, String> environmentVariables = System.getenv();

        return Optional.ofNullable(environmentVariables.get("EMS_HOST"))
                .orElse(configuration.getEmsHost());
    }

    private URI getContextPath() {
        Map<String, String> environmentVariables = System.getenv();
        return Optional.ofNullable(environmentVariables.get("CONTEXT_PATH"))
                .map(this::createUriFromString)
                .orElse(createUriFromString("http://localhost:9002/"));
    }

    private URI createUriFromString(String contextPath) {
        try {
            URI uri = new URI(contextPath);
            LOG.info("Bruker contextPathen " + contextPath);
            return uri;
        } catch (URISyntaxException e) {
            String error = "Buggy context path: " + contextPath;
            LOG.error(error);
            throw new IllegalStateException(error);
        }
    }

}