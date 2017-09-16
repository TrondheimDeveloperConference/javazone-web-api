package no.javazone.api.speaker;

import io.dropwizard.jersey.caching.CacheControl;
import no.javazone.speaker.SpeakerBilde;
import no.javazone.speaker.SpeakerImageCache;

import javax.ws.rs.*;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Path("/speakers/{speakerId}/image")
public class SpeakerResource {

    private final SpeakerImageCache speakerImageCache;

    public SpeakerResource(SpeakerImageCache speakerImageCache) {
        this.speakerImageCache = speakerImageCache;
    }

    @GET
    @Produces("image/png")
    @CacheControl(maxAge = 5, maxAgeUnit = TimeUnit.MINUTES)
    public Response getImage(
            Request request,
            @PathParam("speakerId") String speakerId,
            @QueryParam("size") final String size) {
        SpeakerBilde speakerBilde = speakerImageCache.get(speakerId);
        if (speakerBilde == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        EntityTag entityTag = new EntityTag(String.valueOf(
                Arrays.hashCode(speakerBilde.getStortBilde())
        ));
        Response.ResponseBuilder rb = request
                .evaluatePreconditions(entityTag);
        if(rb != null) {
            return rb.build();
        } else {
            return Response.ok(speakerBilde.getStortBilde())
                    .tag(entityTag)
                    .build();
        }
    }
}
