package com.vkdev.urlshortner.repositories;

import com.vkdev.urlshortner.models.URLEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface URLRepository extends JpaRepository<URLEntity, UUID> {
    List<URLEntity> findByShortCode(String shortCode);
}
