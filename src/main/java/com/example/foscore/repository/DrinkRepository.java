package com.example.foscore.repository;

import com.example.foscore.model.entity.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {

    Optional<Drink> findByName(String name);

}
