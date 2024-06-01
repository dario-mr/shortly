package com.dario.shortly.core.service;

import com.dario.shortly.core.domain.Link;
import com.dario.shortly.core.repository.LinkRepository;
import com.dario.shortly.core.repository.jpa.entity.LinkEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public Optional<Link> findByShortLinkId(String shortLinkId) {
        return linkRepository.findByShortLink(shortLinkId).map(LinkService::toDomain);
    }

    public void save(String longLink, String shortLinkId) {
        linkRepository.save(LinkEntity.builder()
                .longLink(longLink)
                .shortLinkId(shortLinkId)
                .build());
    }

    private static Link toDomain(LinkEntity linkEntity) {
        return new Link(linkEntity.getLongLink(), linkEntity.getShortLinkId());
    }
}
