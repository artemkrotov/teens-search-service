package ru.krotov.teenssearchservice.web.clients;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public final class VkApi {

	private static final String API_VERSION = "5.6";
	private static final String AUTH_URL = "https://oauth.vk.com/authorize"
			+ "?client_id={APP_ID}"
			+ "&scope={PERMISSIONS}"
			+ "&redirect_uri={REDIRECT_URI}"
			+ "&display={DISPLAY}"
			+ "&v={API_VERSION}"
			+ "&response_type=token";
	private static final String API_REQUEST = "https://api.vk.com/method/{METHOD_NAME}"
			+ "?{PARAMETERS}"
			+ "&access_token={ACCESS_TOKEN}"
			+ "&v=" + API_VERSION;
	private final String accessToken;

	// http://oauth.vk.com/authorize?client_id=6764102&display=page&redirect_uri=http://vk.com/blank.html&scope=offline&response_type=token&v=5.60
	private VkApi(String appId, String accessToken) throws IOException {
		this.accessToken = accessToken;
		if (accessToken == null || accessToken.isEmpty()) {
			auth(appId);
			throw new Error("Need access token");
		}
	}

	public static void main(String[] args) throws IOException {
		//VkApi vkApi = new VkApi("6764102", "a2255484130c74ee2cc0e6b192fb8f0b397f79d5fba2bceb092bec7a76fb8675bb3ff5f3ec89f5300e1d8");
		VkApi vkApi = new VkApi("6764102", "");
		//VkApi vkApi = new VkApi("6764102", null);
		System.out.println("m");

	}

	public static VkApi with(String appId, String accessToken) throws IOException {
		return new VkApi(appId, accessToken);
	}

	private static String invokeApi(String requestUrl) throws IOException {
		final StringBuilder result = new StringBuilder();
		final URL url = new URL(requestUrl);
		try (InputStream is = url.openStream()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			reader.lines().forEach(result::append);
		}
		return result.toString();
	}

	private void auth(String appId) throws IOException {
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

	public String getDialogs() throws IOException {
		return invokeApi("messages.getDialogs", null);
	}

	public String getHistory(String userId, int offset, int count, boolean rev) throws IOException {
		return invokeApi("messages.getHistory", Params.create()
				.add("user_id", userId)
				.add("offset", String.valueOf(offset))
				.add("count", String.valueOf(count))
				.add("rev", rev ? "1" : "0"));
	}

	public String getAlbums(String userId) throws IOException {
		return invokeApi("photos.getAlbums", Params.create()
				.add("owner_id", userId)
				.add("photo_sizes", "1")
				.add("thumb_src", "1"));
	}

	private String invokeApi(String method, Params params) throws IOException {
		final String parameters = (params == null) ? "" : params.build();
		String reqUrl = API_REQUEST
				.replace("{METHOD_NAME}", method)
				.replace("{ACCESS_TOKEN}", accessToken)
				.replace("{PARAMETERS}&", parameters);
		return invokeApi(reqUrl);
	}

	private static class Params {

		private final HashMap<String, String> params;

		private Params() {
			params = new HashMap<>();
		}

		public static Params create() {
			return new Params();
		}

		public Params add(String key, String value) {
			params.put(key, value);
			return this;
		}

		public String build() {
			if (params.isEmpty()) return "";
			final StringBuilder result = new StringBuilder();
			params.keySet().stream().forEach(key -> {
				result.append(key).append('=').append(params.get(key)).append('&');
			});
			return result.toString();
		}
	}
}