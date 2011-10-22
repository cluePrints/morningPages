package com.mpages.parsing.crawler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.derby.impl.sql.compile.LengthOperatorNode;
import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class Crawler {
	private static boolean isDebugEnabled = false;
	private static Logger log = LoggerFactory.getLogger(Crawler.class);

	private RegexpDataMapper<String> linksToFollowRegexp;
	private RegexpDataMapper<String[]> dataMapper;

	private List<String> pagesToVisit = Lists.newLinkedList();
	private Set<String> visited = Sets.newHashSet();
	private List<String[]> data = Lists.newLinkedList();
	
	private int maxHits = Integer.MAX_VALUE;

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
		} while (!pagesToVisit.isEmpty() && visited.size() < maxHits);
		logDataCaptured();
	}

	public void logDataCaptured() {
		for (String[] dataPoint : data) {
			log.info(Arrays.toString(dataPoint));
		}
	}
	
	public void setMaxHits(int maxVisits) {
		this.maxHits = maxVisits;
	}
	
	public Set<String> getVisited() {
		return visited;
	}
	
	public List<String[]> getData() {
		return data;
	}

	private void processPage(String url) {
		if (!visited.contains(url)) {
			log.info("Processing: " + url);
			String content = readUrlContent(url);

			List<String> linksToFollow = linksToFollowRegexp.parseData(content);
			pagesToVisit.addAll(linksToFollow);
			log.debug("Added " + linksToFollow.size() + " links to follow.");

			List<String[]> dataFetched = dataMapper.parseData(content);
			data.addAll(dataFetched);
			log.debug("Added " + dataFetched.size() + " data touples.");

			visited.add(url);

			log.info("Progress: " + visited.size() + " done, "
					+ pagesToVisit.size() + " left. ");
		}

		pagesToVisit.remove(url);
	}

	public static String readUrlContent(String urlStr) {
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			PrintWriter wr = new PrintWriter(conn.getOutputStream());
			wr.append("addSearch%5Bdt_id%5D=10&addSearch%5Bd_id%5D=0&addSearch%5Bt_id%5D=0&addSearch%5Bprice_min%5D=&addSearch%5Bprice_max%5D=&addSearch%5Bcurrency%5D=usd&addSearch%5BpriceFor%5D=0&addSearch%5Bfields%5D%5B384%5D%5Bmin%5D=0&addSearch%5Bfields%5D%5B384%5D%5Bmax%5D=0&addSearch%5Bfields%5D%5B388%5D%5Bmin%5D=&addSearch%5Bfields%5D%5B388%5D%5Bmax%5D=&addSearch%5Bfields%5D%5B390%5D%5Bmin%5D=&addSearch%5Bfields%5D%5B390%5D%5Bmax%5D=&addSearch%5Bphone%5D=&addSearch%5Bdays%5D=&fsbmtfReg=%CF%EE%E8%F1%EA");
			wr.flush();
			wr.close();
			InputStream stream = conn.getInputStream();
			
			Charset charset = detectCharset(conn);
			String content = toString(stream, charset);

			return content;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static Charset detectCharset(URLConnection conn) {
		String contentTypeStr = conn.getHeaderField("Content-Type");
		String charsetToken="charset=";
		int startingPos = contentTypeStr.indexOf(charsetToken)+charsetToken.length();
		String encoding=contentTypeStr.substring(startingPos);
		log.debug("Detected character set: " + encoding);
		Charset charset = Charset.forName(encoding);
		return charset;
	}

	private static String toString(InputStream stream, Charset charset) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);

		ByteArrayBuffer baf = new ByteArrayBuffer(5000);
		int current = 0;
		while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		}
		return new String(baf.toByteArray(), charset);
	}
}
