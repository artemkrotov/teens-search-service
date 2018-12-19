package ru.krotov.teenssearchservice.components.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.WallPostFullFilterExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class StopWordWallPostFullFilter extends AbstractWallPostFullFilter {

	// TODO: Реализовать через свойства
	private List<String> stopWords = Arrays.asList("трах", "сосать", "сосну", "минет", "наркотики",
			"гаш", "фен", "спайс", "ехи", "карт", "помогите", "меф", "лед", "быстрый", "подар", "прод", "платить", "жар", "вспомина",
			"куплю", "продаж", "покупк", "трава", "услуг", "кайфушк", "массаж", "прыгну", "почувствова", "трус", "надевать",
			"деньг", "денюж", "денеж", "подкиньте", "займите", "накаж", "наказ", "плохой", "плохая", "ласк", "нежность", "жар");

	public StopWordWallPostFullFilter(WallPostFullFilterExecutor filter) {
		super(filter);
	}

	// TODO: Может работать долго
	@Override
	public boolean filter(WallPostFull wallPostFull) {

		String text = wallPostFull.getText();
		return filter(text);
	}

	@Override
	public String getErrorMessage(WallPostFull wallPostFull) {
		return String.format("Message with id = %s have stopWord", wallPostFull.getId());
	}

	public boolean filter(String text) {
		text = text.toLowerCase();
		return Stream.of(text.split(" "))
				.noneMatch(word -> stopWords.stream()
						.anyMatch(word::contains)
				);
	}

	@Override
	public int getOrder() {
		return 200;
	}
}
