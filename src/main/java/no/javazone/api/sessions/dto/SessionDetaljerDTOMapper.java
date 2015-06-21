package no.javazone.api.sessions.dto;

import no.javazone.api.links.LinkDTO;
import no.javazone.api.links.LinkDTOMapper;
import no.javazone.api.speaker.SpeakerBildeUriCreator;
import no.javazone.sessions.Session;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SessionDetaljerDTOMapper {
    public static SessionDetaljerDTO toSessionDetaljerDTO(Session session, URI contextRoot) {
        return new SessionDetaljerDTO(
                session.getTittel(),
                session.getSlot().getStarter(),
                session.getSlot().getStopper(),
                session.getNiva(),
                session.getOppsummering(),
                session.getBeskrivelse(),
                mapToForedragsholderDetaljerDTOs(session, contextRoot),
                createLinks(session),
                session.getRom(),
                session.getNokkelord(),
                session.getTiltenktPublikum());
    }

    private static List<ForedragsholderDetaljerDTO> mapToForedragsholderDetaljerDTOs(
            Session session, URI contextRoot) {
        return session.getForedragsholdere()
                .stream()
                .map(foredragsholder -> new ForedragsholderDetaljerDTO(
                        foredragsholder.getNavn(),
                        foredragsholder.getBio(),
                        SpeakerBildeUriCreator.createBildeUrl(foredragsholder, contextRoot)))
                .collect(Collectors.toList());
    }

    private static List<LinkDTO> createLinks(Session session) {
        ArrayList<LinkDTO> links = new ArrayList<>();

        links.add(LinkDTOMapper.toLinkDTO("video", session.getVideoUri()));

        return links;
    }

}
