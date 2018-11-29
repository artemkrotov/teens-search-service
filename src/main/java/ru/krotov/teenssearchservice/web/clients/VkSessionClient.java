package ru.krotov.teenssearchservice.web.clients;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.queries.users.UserField;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.krotov.teenssearchservice.web.UserDto;

import java.awt.*;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class VkSessionClient {


	private static final String API_VERSION = "5.6";
	private static final String AUTH_URL = "https://oauth.vk.com/authorize"
			+ "?client_id={APP_ID}"
			+ "&scope={PERMISSIONS}"
			+ "&redirect_uri={REDIRECT_URI}"
			+ "&display={DISPLAY}"
			+ "&v={API_VERSION}"
			+ "&response_type=token";
	;


	private static String PROXY_HOST = "sr123.spry.fail" /* proxy host */;
	private static Integer PROXY_PORT = 1080; /* proxy port */
	private static String PROXY_USER = "telegram"; /* proxy port */
	private static String PROXY_PASSWORD = "telegram"; /* proxy port */

	public static void main(String[] args) throws ClientException, ApiException {
		VkApiClient vk = new VkApiClient(new HttpTransportClient());
		UserActor userActor = new UserActor(6764102, "a2255484130c74ee2cc0e6b192fb8f0b397f79d5fba2bceb092bec7a76fb8675bb3ff5f3ec89f5300e1d8");
		vk.groups();


		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
			}
		});

		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
			botOptions.setProxyHost(PROXY_HOST);
			botOptions.setProxyPort(PROXY_PORT);
			botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);



			Bot bot = new Bot(botOptions);
			telegramBotsApi.registerBot(bot);


			List<UserDto> userDtos = new ArrayList<>();

			//TODO: можно сразу на ID переделать и передавать их в конфиге
			for (String groupId : GroupsHelper.groupIds) {
				// TODO: Сделать проверки на название группы и т.д.
				vk.groups().getById(userActor).groupId(groupId).execute().stream().findAny().ifPresent(groupFull -> {
					try {
						List<WallPostFull> wallPostFulls = vk.wall().get(userActor).domain("vpiski_moscow").execute().getItems();
						kwait();
						wallPostFulls.forEach(wallPostFull -> {
							Integer fromId = wallPostFull.getFromId();
							try {
								kwait();
								vk.users().get(userActor).userIds(String.valueOf(fromId)).fields(Arrays.asList(UserField.values())).execute().stream()
										.filter(userXtrCounters -> WomenNames.isWoman(userXtrCounters.getFirstName()))
										.findAny()
										.ifPresent(userXtrCounters -> {

											kwait();
											UserDto userDto = new UserDto();

											userDto.setFullName(userXtrCounters.getFirstName() + " " + userXtrCounters.getLastName());
											userDto.setInstagram(userXtrCounters.getInstagram());
											userDto.setMessage(wallPostFull.getText());


											bot.sendMsg("281490960", userDto.toString());


										});

							} catch (ApiException | ClientException e) {
								log.error(e.getMessage());
							}
						});

					} catch (ApiException | ClientException e) {
						log.error(e.getMessage());
					}
				});
			}


//		wallPostFulls.stream()
//				.filter(Objects::nonNull) // TODO БУДУТ Объекты реализующие интерфейс интерфейс Filter
//				.forEach();
//		;

			//Integer fromId = wallPostFulls.get(1).getFromId();

			// TODO: Когда сообщение от имени группы FromId принадлежит группе
			//	vk.users().get(userActor).userIds(String.valueOf(fromId)).fields(Arrays.asList(UserField.values())).execute().get(0);

		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}
	}

	private static void kwait() {
		try {
			Thread thread = Thread.currentThread();

			synchronized (thread) {
				thread.wait(500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void getSession(String appId) throws IOException {
		String reqUrl = AUTH_URL
				.replace("{APP_ID}", appId)
				.replace("{PERMISSIONS}", "photos,messages,offline,groups")
				.replace("{REDIRECT_URI}", "http://vk.com/blank.html")
				.replace("{DISPLAY}", "page")
				.replace("{API_VERSION}", API_VERSION);
		try {
			Desktop.getDesktop().browse(new URL(reqUrl).toURI());
		} catch (URISyntaxException ex) {
			throw new IOException(ex);
		}
	}
}
