package me.matule.backend.repository;

import me.matule.backend.data.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser_Id(Long id);

    Optional<CartItem> findByUser_IdAndProduct_Id(Long id, Long id1);
}