package no.javazone.sessions;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public class Foredragsholder {
    private final String speakerId;
    private final String navn;
    private final String bio;
    private final URI gravatarUri;
    private final URI photoUri;

    public Foredragsholder(
            String speakerId,
            String navn,
            String bio,
            Optional<URI> photoUri,
            Optional<URI> gravatarUri) {
        this.speakerId = speakerId;
        this.navn = navn;
        this.bio = bio;
        this.photoUri = photoUri.orElse(null);
        this.gravatarUri = gravatarUri.orElse(null);
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public String getNavn() {
        return navn;
    }

    public String getBio() {
        return bio;
    }

    public Optional<URI> getPhotoUri() {
        return Optional.ofNullable(photoUri);
    }

    public Optional<URI> getGravatarUri() {
        return Optional.ofNullable(gravatarUri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foredragsholder that = (Foredragsholder) o;
        return Objects.equals(speakerId, that.speakerId) &&
                Objects.equals(navn, that.navn) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(gravatarUri, that.gravatarUri) &&
                Objects.equals(photoUri, that.photoUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speakerId, navn, bio, gravatarUri, photoUri);
    }
}
