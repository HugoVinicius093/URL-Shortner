package com.hugo.urlshortner.repository;

import com.hugo.urlshortner.model.entities.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<UrlEntity, Long> {

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);

    Optional<UrlEntity> findByShortnedUrl(String shortnedUrl);

}
