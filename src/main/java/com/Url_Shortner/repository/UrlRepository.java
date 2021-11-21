package com.Url_Shortner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Url_Shortner.model.UrlEntity;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    @Query("SELECT u FROM url u WHERE u.fullUrl = ?1")
    List<UrlEntity> findUrlByFullUrl(String fullUrl);
    
    @Query("SELECT u FROM url u WHERE u.shortUrl = ?1")
	Optional<UrlEntity> findByShortUrl(String fullUrl);
}