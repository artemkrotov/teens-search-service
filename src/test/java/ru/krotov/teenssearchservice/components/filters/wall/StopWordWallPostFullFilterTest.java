package ru.krotov.teenssearchservice.components.filters.wall;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.krotov.teenssearchservice.components.filters.WallPostFullFilterExecutor;

import java.util.Arrays;
import java.util.List;

public class StopWordWallPostFullFilterTest {

	private StopWordWallPostFullFilter stopWordWallPostFullFilter = new StopWordWallPostFullFilter(new WallPostFullFilterExecutor());

	@Test
	@Ignore
	public void filter() {

		List<String> expectedStopWords = Arrays.asList("трах", "сосать", "сосну", "минет", "наркотики",
				"гаш", "фен", "спайс", "ехи", "меф", "лед", "Трахнул",  "быстрый", "продам", "куплю", "продаж", "покупк", "трава", "услуг", "массаж");


		Assert.assertTrue(expectedStopWords.stream().noneMatch(stopWordWallPostFullFilter::filter));

	}


}