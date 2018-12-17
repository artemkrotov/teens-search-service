package ru.krotov.teenssearchservice.components.filters.user;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.UserFilterExecutor;

import java.time.LocalDate;
import java.time.Period;

@Component
public class AgeUserFilter extends AbstractUserFilter {

	private static final int MIN_AGE = 18;

	public AgeUserFilter(UserFilterExecutor filter) {
		super(filter);
	}

	// TODO: Дубликат TelegramUserDto
	@Override
	public boolean filter(UserXtrCounters userXtrCounters) {

		String bDay = userXtrCounters.getBdate();
		if (StringUtils.isEmpty(bDay)) {
			return true;
		}

		String[] split = bDay.split("\\.");

		int monthOfBirthDay = Integer.parseInt(split[1]);
		int dayOfBirthDay = Integer.parseInt(split[0]);
		if (split.length < 3) {
			return true;
		}

		int yearOfBirthDay = Integer.parseInt(split[2]);
		int age = calculateAge(yearOfBirthDay, monthOfBirthDay, dayOfBirthDay);

		return age >= MIN_AGE;
	}

	@Override
	public String getErrorMessage(UserXtrCounters userXtrCounters) {
		return String.format("User's with id = %d age is too small! Minimal age is %d", userXtrCounters.getId(), MIN_AGE);
	}

	private int calculateAge(int year, int month, int day) {

		LocalDate birthDay = LocalDate.of(year, month, day);
		LocalDate now = LocalDate.now();

		return Period.between(birthDay, now).getYears();
	}

	@Override
	public int getOrder() {
		return 200;
	}
}
