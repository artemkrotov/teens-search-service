package ru.krotov.teenssearchservice.components.clients.telegram.dto;

import lombok.Data;

@Data
public class TelegramMessageDto {

	private String message;
	private TelegramUserDto telegramUserDto;
}
