package ru.krotov.teenssearchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.krotov.teenssearchservice.model.StopWord;
import ru.krotov.teenssearchservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<Long, User> {
}
