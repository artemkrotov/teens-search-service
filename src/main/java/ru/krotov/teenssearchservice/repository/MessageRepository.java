package ru.krotov.teenssearchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.krotov.teenssearchservice.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Long, Message> {

	@Query(value = "SELECT m FROM Message m WHERE m.ID = ?1 ORDER BY m.CREATED ASC LIMIT 1", nativeQuery = true)
	Message findLastMessageByUserId (Integer userId);

}
