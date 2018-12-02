package ru.krotov.teenssearchservice.web.clients;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


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

	public void getToken(Integer appId) throws IOException {
		String reqUrl = AUTH_URL
				.replace("{APP_ID}", String.valueOf(appId))
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
