package com.mpages.parsing.crawler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;

public class Crawler {
	private Logger log = LoggerFactory.getLogger(Crawler.class);
	
	private RegexpDataMapper<String> linksToFollowRegexp;
	private RegexpDataMapper<String[]> dataMapper;
	
	private List<String> pagesToVisit = Lists.newLinkedList();
	private Set<String> visited = Sets.newHashSet();
	private List<String[]> data = Lists.newLinkedList();
	
	public Crawler(String dataRegexp, String linksToFollowRegexp,
			String startingPage) {
		super();
		this.dataMapper = new ToArrayRegexpMapper(dataRegexp); 
		this.linksToFollowRegexp = new ToStringRegexpMapper(linksToFollowRegexp);
		this.pagesToVisit.add(startingPage);
	}
	
	public void start() {
		do {
			processPage(pagesToVisit.get(0));
		} while (!pagesToVisit.isEmpty());
	}
	
	private void processPage(String url) {
		log.info("Processing: "+url);
		
		if (!visited.contains(url)) {
			String content = readUrlContent(url);
			log.debug("Successfully fetched content");
			
			List<String> linksToFollow = linksToFollowRegexp.parseData(content);
			pagesToVisit.addAll(linksToFollow);
			log.debug("Added "+linksToFollow.size()+" links to follow.");
			
			List<String[]> dataFetched = dataMapper.parseData(content);
			data.addAll(dataFetched);
			log.debug("Added "+dataFetched.size()+" data touples.");
			
			pagesToVisit.remove(url);
			visited.add(url);
			
			log.info("Progress: "+visited.size()+" done, "+pagesToVisit.size()+" left.");
		}
	}
	

	public static String readUrlContent(String urlStr) {
		try {
			URL url = new URL(urlStr);
			InputStream stream = url.openConnection().getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(stream));
			String res = CharStreams.toString(r);			
			return res;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
