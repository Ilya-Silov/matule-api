package me.matule.backend.repository;

import me.matule.backend.data.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUser_IdAndProduct_Id(Long id, Long id1);
}