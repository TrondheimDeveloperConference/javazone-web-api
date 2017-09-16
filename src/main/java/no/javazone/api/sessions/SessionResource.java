package no.javazone.api.sessions;

import io.dropwizard.jersey.caching.CacheControl;
import no.javazone.api.sessions.dto.SessionDTOMapper;
import no.javazone.api.sessions.dto.SessionDetaljerDTOMapper;
import no.javazone.devnull.DevNullUriCreator;
import no.javazone.http.PathResolver;
import no.javazone.sessions.Event;
import no.javazone.sessions.Session;
import no.javazone.sessions.SessionId;
import no.javazone.sessions.SessionRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Path("/events/{eventId}/sessions")
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource {

    private PathResolver pathResolver;
    private SessionRepository sessionRepository;
    private DevNullUriCreator devNullUriCreator;

    @Context
    private UriInfo uriInfo;

    public SessionResource(
            PathResolver pathResolver,
            SessionRepository sessionRepository,
            DevNullUriCreator devNullUriCreator) {
        this.pathResolver = pathResolver;
        this.sessionRepository = sessionRepository;
        this.devNullUriCreator = devNullUriCreator;
    }

    @GET
    @CacheControl(maxAge = 5, maxAgeUnit = TimeUnit.MINUTES)
    public Response getSessions(Request request,
                                @PathParam("eventId") String eventSlug,
                                @HeaderParam("X-Forwarded-Proto") String forwardedProto) {
        Optional<Event> eventOptional = sessionRepository.getSessions(eventSlug);
        URI contextRoot = resolveContextRoot(forwardedProto);

        return eventOptional
                .map(x -> {
                    EntityTag entityTag = new EntityTag(String.valueOf(x.hashCode()));
                    Response.ResponseBuilder rb = request
                            .evaluatePreconditions(entityTag);
                    if(rb != null) {
                        return rb.build();
                    } else {
                        return Response.ok()
                                .entity(SessionDTOMapper.toSessionDTOs(
                                        x,
                                        pathResolver.path(uriInfo),
                                        contextRoot,
                                        devNullUriCreator))
                                .tag(entityTag)
                                .build();
                    }

                })
                .orElse(errorResponse("Event not found"));
    }

    @GET
    @Path("/{sessionId}")
    @CacheControl(maxAge = 5, maxAgeUnit = TimeUnit.MINUTES)
    public Response getSession(
            Request request,
            @PathParam("eventId") String eventSlug,
            @PathParam("sessionId") String sessionId,
            @HeaderParam("X-Forwarded-Proto") String forwardedProto
    ) {
        Optional<Session> sessionOptional = sessionRepository.getSession(eventSlug, new SessionId(sessionId));
        URI contextRoot = resolveContextRoot(forwardedProto);
        return sessionOptional
                .map(session -> {
                    EntityTag entityTag = new EntityTag(String.valueOf(session.hashCode()));
                    Response.ResponseBuilder rb = request
                            .evaluatePreconditions(entityTag);
                    if(rb != null) {
                        return rb.build();
                    } else {
                        return Response.ok()
                                .entity(SessionDetaljerDTOMapper
                                        .toSessionDetaljerDTO(session, contextRoot, devNullUriCreator))
                                .tag(entityTag)
                                .build();
                    }
                })
                .orElse(errorResponse("Session not found"));
    }

    private Response errorResponse(String errorMessage) {
        return Response.status(404).type(MediaType.APPLICATION_JSON_TYPE).entity("{\"message\": \"" + errorMessage + "\"}").build();
    }


    private URI resolveContextRoot(@HeaderParam("X-Forwarded-Proto") String forwardedProto) {
        return UriBuilder.fromUri(pathResolver.getContextRoot())
                .scheme(forwardedProto != null ? forwardedProto : uriInfo.getRequestUri().getScheme()).build();
    }


}
