package ru.krotov.teenssearchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.krotov.teenssearchservice.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query(value = "SELECT * FROM message m WHERE m.id = ?1 ORDER BY m.created ASC LIMIT 1", nativeQuery = true)
	Message findLastMessageByUserId (Integer userId);

}
