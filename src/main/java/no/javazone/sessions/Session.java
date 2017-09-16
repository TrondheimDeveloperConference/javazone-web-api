package no.javazone.sessions;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Session {
    private final SessionId id;
    private final String tittel;
    private final String format;
    private final Slot slot;
    private final List<Foredragsholder> foredragsholdere;
    private final String sprak;
    private final String niva;
    private final String oppsummering;
    private final String beskrivelse;
    private final Optional<URI> videoUri;
    private final String rom;
    private final List<String> nokkelord;
    private final String tiltenktPublikum;
    private final EmsIds emsIds;

    public Session(
            SessionId id,
            String tittel,
            String format,
            Slot slot,
            List<Foredragsholder> foredragsholdere,
            String sprak,
            String niva,
            String oppsummering,
            String beskrivelse,
            Optional<URI> videoUri,
            String rom,
            List<String> nokkelord,
            String tiltenktPublikum,
            EmsIds emsIds) {
        this.id = id;
        this.tittel = tittel;
        this.format = format;
        this.slot = slot;
        this.foredragsholdere = foredragsholdere;
        this.sprak = sprak;
        this.niva = niva;
        this.oppsummering = oppsummering;
        this.beskrivelse = beskrivelse;
        this.videoUri = videoUri;
        this.rom = rom;
        this.nokkelord = nokkelord;
        this.tiltenktPublikum = tiltenktPublikum;
        this.emsIds = emsIds;
    }

    public SessionId getId() {
        return id;
    }

    public String getTittel() {
        return tittel;
    }

    public List<Foredragsholder> getForedragsholdere() {
        return foredragsholdere;
    }

    public String getFormat() {
        return format;
    }

    public Slot getSlot() {
        return slot;
    }

    public String getSprak() {
        return sprak;
    }

    public String getNiva() {
        return niva;
    }

    public String getOppsummering() {
        return oppsummering;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public Optional<URI> getVideoUri() {
        return videoUri;
    }

    public String getRom() {
        return rom;
    }

    public List<String> getNokkelord() {
        return nokkelord;
    }

    public String getTiltenktPublikum() {
        return tiltenktPublikum;
    }

    public EmsIds getEmsIds() {
        return emsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id) &&
                Objects.equals(tittel, session.tittel) &&
                Objects.equals(format, session.format) &&
                Objects.equals(slot, session.slot) &&
                Objects.equals(foredragsholdere, session.foredragsholdere) &&
                Objects.equals(sprak, session.sprak) &&
                Objects.equals(niva, session.niva) &&
                Objects.equals(oppsummering, session.oppsummering) &&
                Objects.equals(beskrivelse, session.beskrivelse) &&
                Objects.equals(videoUri, session.videoUri) &&
                Objects.equals(rom, session.rom) &&
                Objects.equals(nokkelord, session.nokkelord) &&
                Objects.equals(tiltenktPublikum, session.tiltenktPublikum) &&
                Objects.equals(emsIds, session.emsIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tittel, format, slot, foredragsholdere, sprak, niva, oppsummering, beskrivelse, videoUri, rom, nokkelord, tiltenktPublikum, emsIds);
    }
}
