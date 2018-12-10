package ru.krotov.teenssearchservice.components.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import org.junit.Assert;
import org.junit.Test;
import ru.krotov.teenssearchservice.components.filters.Filter;

import java.util.Arrays;
import java.util.List;

public class StopWordWallPostFullFilterTest {

	StopWordWallPostFullFilter stopWordWallPostFullFilter = new StopWordWallPostFullFilter(new Filter<WallPostFull>() {
		@Override
		public void register(Filter<WallPostFull> filter) {

		}

		@Override
		public boolean filter(WallPostFull wallPostFull) {
			return false;
		}
	});

	@Test
	public void filter() {

		List<String> expectedStopWords = Arrays.asList("трах", "сосать", "сосну", "минет", "наркотики",
				"гаш", "фен", "спайс", "ехи", "меф", "лед", "Трахнул",  "быстрый", "продам", "куплю", "продаж", "покупк", "трава", "услуг", "массаж");


		Assert.assertTrue(expectedStopWords.stream().noneMatch(stopWordWallPostFullFilter::filter));

	}


}