package no.javazone.ems;

import net.hamnaberg.json.Collection;
import net.hamnaberg.json.Item;
import net.hamnaberg.json.Link;
import net.hamnaberg.json.parser.CollectionParser;
import no.javazone.sessions.Event;
import no.javazone.sessions.Foredragsholder;
import no.javazone.sessions.Session;
import no.javazone.sessions.SessionId;
import no.javazone.speaker.NestedTimer;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.javazone.ems.EmsIdMapper.mapToEmsIds;
import static no.javazone.ems.ItemMappers.*;

public class EmsAdapter {

    private static final int CONNECT_TIMOUT_IN_MILLISECONDS = 30 * 1000;
    private static final int READ_TIMOUT_IN_MILLISECONDS = 30 * 1000;

    private final WebTarget emsWebTarget;
    private final Client client;

    public EmsAdapter(String emsHost) {
        client = ClientBuilder.newClient();

        client.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMOUT_IN_MILLISECONDS);
        client.property(ClientProperties.READ_TIMEOUT, READ_TIMOUT_IN_MILLISECONDS);

        emsWebTarget = client.target("http://" + emsHost);
    }

    public Stream<Event> getEvents(NestedTimer timer) {
        return getEventUris()
                .parallelStream()
                .map(x -> this.getEvent(x, timer))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public void check() {
        WebTarget target = emsWebTarget.path("/server/app-info");
        target.request().buildGet().invoke(String.class);
    }

    private List<EventMinimal> getEventUris() {
        WebTarget eventWebTarget = emsWebTarget.path("/server/events");

        String response = eventWebTarget.request().buildGet().invoke(String.class);

        try {
            Collection collection = new CollectionParser().parse(response);
            return mapToEventMinimals(collection);

        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private Optional<Event> getEvent(EventMinimal eventMinimal, NestedTimer timer) {
        return timer.time("getEvent", eventMinimal.getSlug(), () -> {
                WebTarget sessionWebTarget = client
                .target(eventMinimal.getUri())
                .path("sessions");

            String response = sessionWebTarget.request().buildGet().invoke(String.class);

            try {
                Collection collection = new CollectionParser().parse(response);
                return Optional.of(mapToEvent(collection, eventMinimal.getSlug(), timer));

            } catch (IOException e) {
                return Optional.empty();
            }
        });

    }

    private List<EventMinimal> mapToEventMinimals(Collection collection) {
        return collection
                .getItems()
                .stream()
                .map(this::mapItemToEventMinimal)
                .collect(Collectors.toList());
    }

    private EventMinimal mapItemToEventMinimal(Item item) {
        return new EventMinimal(
                item.getHref().get(),
                mapPropertyToString(item, "slug"));
    }

    private Event mapToEvent(Collection collection, String slug, NestedTimer timer) throws IOException {
        List<Session> sessions = collection
                .getItems()
                .stream()
                .map(x -> this.mapItemTilForedrag(x, timer))
                .collect(Collectors.toList());
        return new Event(sessions, slug);
    }

    private Session mapItemTilForedrag(Item item, NestedTimer timer) {
        return new Session(
                new SessionId(generateIdString(item)),
                mapPropertyToString(item, "title"),
                mapPropertyToString(item, "format"),
                SlotMapper.mapToSlot(item),
                getForedragsholdere(item.linkByRel("speaker collection"), timer),
                mapPropertyToString(item, "lang"),
                mapPropertyToString(item, "level"),
                mapPropertyToString(item, "summary"),
                mapPropertyToString(item, "body"),
                mapLink(item, "alternate video"),
                mapLinkPrompt(item, "room item"),
                mapPropertyToList(item, "keywords"),
                mapPropertyToString(item, "audience"),
                mapToEmsIds(item));
    }

    private List<Foredragsholder> getForedragsholdere(Optional<Link> link, NestedTimer timer) {
        if (link.isPresent()) {
            return timer.time("getForedragsholder", null, () -> {
                WebTarget webTarget = client
                        .target(link.get().getHref());
                String response = webTarget.request().buildGet().invoke(String.class);

                try {
                    Collection collection = new CollectionParser().parse(response);
                    return collection
                            .getItems()
                            .stream()
                            .map(EmsForedragsholderMapper::mapItemTilForedragsholder)
                            .collect(Collectors.toList());
                } catch (IOException e) {
                    throw new RuntimeException("Finner ikke speakers");
                }
            });
        } else {
            throw new RuntimeException("Speakerlink finnes ikke");
        }
    }

}
