package no.javazone.http;

public class SubdomeneSjekker {
    static boolean erEtJavaZoneSubdomene(String origin) {
        return origin.matches("^http[s]?:/\\/.*trondheimdc.no$");
    }
}
