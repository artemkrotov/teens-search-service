package ru.krotov.teenssearchservice.web.dto;

import com.vk.api.sdk.objects.base.BaseObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.Period;

@Data
public class UserDto {

	static final int MINIMUM_YEAR_OF_BIRTH = 1960;

	private String fullName;
	private String vk;
	private String instagram;
	private String photoUrl;
	private String city;
	private Integer age;
	private boolean todayBirthDay;

	public void setCityIfPresent (BaseObject city) {
		if (city != null) {
			this.setCity(city.getTitle());
		}
	}

	public void setAgeAndTodayBirthDay(String bDay) {

		if (StringUtils.isEmpty(bDay)) {
			return;
		}

		String[] split = bDay.split("\\.");

		int monthOfBirthDay = Integer.parseInt(split[1]);
		int dayOfBirthDay = Integer.parseInt(split[0]);

		LocalDate now = LocalDate.now();

		this.setTodayBirthDay(monthOfBirthDay, dayOfBirthDay, now.getMonthValue(), now.getDayOfMonth());
		if (split.length < 3) {
			return;
		}

		int yearOfBirthDay = Integer.parseInt(split[2]);
		if (yearOfBirthDay < MINIMUM_YEAR_OF_BIRTH) {
			return;
		}

		setAge(calculateAge(yearOfBirthDay, monthOfBirthDay, dayOfBirthDay));
	}

	private int calculateAge(int year, int month, int day) {

		LocalDate birthDay = LocalDate.of(year, month, day);
		LocalDate now = LocalDate.now();

		return Period.between(birthDay, now).getYears();
	}

	private void setTodayBirthDay(int monthOfBirthDay, int dayOfBirthDay, int day, int month) {
		todayBirthDay = monthOfBirthDay == month && dayOfBirthDay == day;
	}
}
