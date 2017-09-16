package no.javazone.sessions;

import java.util.Objects;

public class Slot {
    private final String starter;
    private final String stopper;

    public Slot(String starter, String stopper) {
        this.starter = starter;
        this.stopper = stopper;
    }

    public String getStarter() {
        return starter;
    }

    public String getStopper() {
        return stopper;
    }

    public static Slot tom() {
        return new Slot(null, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(starter, slot.starter) &&
                Objects.equals(stopper, slot.stopper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(starter, stopper);
    }
}
