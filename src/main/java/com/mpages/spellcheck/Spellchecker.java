package com.mpages.spellcheck;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Spellchecker {
	String pattern = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
	"<spellrequest textalreadyclipped=\"0\" ignoredups=\"0\" ignoredigits=\"1\" ignoreallcaps=\"1\">" +
	    "<text>%s</text>" +
	"</spellrequest>";
	
	public boolean isOk(String text) throws Exception
	{
		URL url; 
		HttpURLConnection urlConn; 
		DataOutputStream printout; 
		BufferedReader input;
		url =new URL("http://www.google.com/tbproxy/spell?lang=en&hl=en"); 
		urlConn =(HttpURLConnection)url.openConnection(); 
		urlConn.setDoInput(true); 
		urlConn.setDoOutput(true); 
		urlConn.setUseCaches(false);

		urlConn.setRequestMethod("POST"); // set request type

		String content = String.format(pattern, text); 
		urlConn.setRequestProperty("Content-Length", content.length()+ "" ); 
		urlConn.setRequestProperty("Content-Type","text/xml" );

		printout = new DataOutputStream( urlConn.getOutputStream() ); 
		input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		System.out.println(content); 
		printout.writeBytes(content); 
		printout.flush (); 
		printout.close ();

	    String str; while( null != ((str = input.readLine())))  {
	    	System.out.println(str);
	    }
	    input.close (); 
		return true;
	}
}
