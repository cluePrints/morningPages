package com.mpages.parsing.crawler;

import org.junit.Test;

public class CrawlerIntegrationTest {
	private static final String LINKS_REGEXP = "href=\"(.*ru/offer/ad/.*/kiev/page.*)\">";
	private static final String DATA_REGEXP = "кий</td>\\W+<td   nowrap>(.{0,400})</td>.{0,800}<a href=\"(.{0,100})\" target=\"_blank\">подробнее</a>";

	@Test
	public void test()
	{
		Crawler crawler = new Crawler(DATA_REGEXP, LINKS_REGEXP, "http://www.svdevelopment.com/ru/offer/ad/10/kiev/page/11/");
		crawler.start();
	}
}
