package ru.krotov.teenssearchservice.configurations.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.configurations.filters.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class StopWordWallPostFullFilter extends AbstractWallPostFullFilter {

	// TODO: Реализовать через свойства
	private List<String> stopWords = Arrays.asList("трах", "сосать", "сосну", "минет", "наркотики",
			"гаш", "фен", "спайс", "ехи", "меф", "лед", "быстрый", "продам", "куплю", "продаж", "покупк", "трава", "услуг", "массаж");

	public StopWordWallPostFullFilter(@Qualifier("wallPostFullFilterExecutor") Filter<WallPostFull> filter) {
		super(filter);
	}

	// TODO: Может работать долго
	@Override
	public boolean filter(WallPostFull wallPostFull) {
		String text = wallPostFull.getText();

		return Stream.of(text.split(" "))
				.noneMatch(word -> stopWords.stream()
						.anyMatch(word::contains)
				);
	}
}
