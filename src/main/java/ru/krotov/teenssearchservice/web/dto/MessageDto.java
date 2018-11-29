package ru.krotov.teenssearchservice.web.dto;

import lombok.Data;

@Data
public class MessageDto {

	private String message;
	private UserDto userDto;
}
