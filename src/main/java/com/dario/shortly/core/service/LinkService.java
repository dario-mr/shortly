package com.dario.shortly.core.service;

import com.dario.shortly.core.domain.Link;
import com.dario.shortly.repository.LinkRepository;
import com.dario.shortly.repository.jpa.entity.LinkEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public Optional<Link> findByShortLinkId(String shortLinkId) {
        return linkRepository.findByShortLink(shortLinkId).map(LinkService::toDomain);
    }

    @Async
    public CompletableFuture<Void> save(String longLink, String shortLinkId) {
        return CompletableFuture.runAsync(() ->
                linkRepository.save(LinkEntity.builder()
                        .longLink(longLink)
                        .shortLinkId(shortLinkId)
                        .build()));
    }

    private static Link toDomain(LinkEntity linkEntity) {
        return new Link(linkEntity.getLongLink(), linkEntity.getShortLinkId());
    }
}
