package ru.krotov.teenssearchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.krotov.teenssearchservice.model.StopWord;

@Repository
public interface StopWordRepository extends JpaRepository<Long, StopWord> {
}
