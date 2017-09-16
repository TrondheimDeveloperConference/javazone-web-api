package no.javazone.sessions;

import java.util.Objects;
import java.util.UUID;

public class EmsIds {

    private UUID eventId;
    private UUID sessionId;

    public EmsIds(UUID eventId, UUID sessionId) {
        this.eventId = eventId;
        this.sessionId = sessionId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmsIds emsIds = (EmsIds) o;
        return Objects.equals(eventId, emsIds.eventId) &&
                Objects.equals(sessionId, emsIds.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, sessionId);
    }
}
