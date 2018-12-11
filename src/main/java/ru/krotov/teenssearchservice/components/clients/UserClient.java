package ru.krotov.teenssearchservice.components.clients;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;

public interface UserClient {
	UserXtrCounters getUser(WallPostFull wallPostFull);
}
