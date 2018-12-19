package ru.krotov.teenssearchservice.components.filters.wall;

import com.google.common.base.Utf8;
import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.WallPostFullFilterExecutor;

@Component
public class MessageLengthWallPostFullFilter extends AbstractWallPostFullFilter {

	private static final int MAX_MESSAGE_LENGTH = 4096;
	public MessageLengthWallPostFullFilter(WallPostFullFilterExecutor filter) {
		super(filter);
	}

	@Override
	public boolean filter(WallPostFull wallPostFull) {
		return Utf8.encodedLength(wallPostFull.getText()) <= MAX_MESSAGE_LENGTH;
	}

	@Override
	public String getErrorMessage(WallPostFull wallPostFull) {
		return String.format("Message with id = %d length > %d (%d)",
				wallPostFull.getId(), MAX_MESSAGE_LENGTH, wallPostFull.getText().length());
	}

	@Override
	public int getOrder() {
		return 50;
	}
}
