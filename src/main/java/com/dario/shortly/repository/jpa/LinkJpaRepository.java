package com.dario.shortly.repository.jpa;

import com.dario.shortly.repository.jpa.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkJpaRepository extends JpaRepository<LinkEntity, Long> {

    Optional<LinkEntity> findByShortLinkId(String shortLinkId);
}
