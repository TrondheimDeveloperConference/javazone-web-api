package no.javazone.sessions;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Event {

    private final String slug;
    private final Map<SessionId, Session> sessions;

    public Event(List<Session> sessions, String slug) {
        this.sessions = sessions
                .stream()
                .collect(Collectors.toMap(
                        Session::getId,
                        Function.identity()));

        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public Collection<Session> getSessions() {
        return sessions.values();
    }

    public Optional<Session> findSessionById(SessionId sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(slug, event.slug) &&
                Objects.equals(sessions, event.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug, sessions);
    }
}
