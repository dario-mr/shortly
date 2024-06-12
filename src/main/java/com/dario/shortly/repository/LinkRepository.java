package com.dario.shortly.repository;

import com.dario.shortly.repository.jpa.LinkJpaRepository;
import com.dario.shortly.repository.jpa.entity.LinkEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LinkRepository {

    private final LinkJpaRepository linkJpaRepository;

    public Optional<LinkEntity> findByShortLink(String shortLink) {
        return linkJpaRepository.findByShortLinkId(shortLink);
    }

    public void save(LinkEntity linkEntity) {
        linkJpaRepository.save(linkEntity);
    }

}
