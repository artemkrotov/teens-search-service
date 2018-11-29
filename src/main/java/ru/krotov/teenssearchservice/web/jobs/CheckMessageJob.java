package ru.krotov.teenssearchservice.web.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


// TODO: Проанализировать актуальность применения Spring Batch
@Component
public class CheckMessageJob {

	// TODO: Вынести в конфиг
	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void checkMessages () {
		// todosomething
	}

}
