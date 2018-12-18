package ru.krotov.teenssearchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.krotov.teenssearchservice.model.Message;

import javax.transaction.Transactional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	@Transactional
	@Query(value = "SELECT * FROM message m WHERE m.user_id = ?1 ORDER BY m.created ASC LIMIT 1", nativeQuery = true)
	Message findLastMessageByUserId (Integer userId);

}
