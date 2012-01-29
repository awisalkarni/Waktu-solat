package com.awis.waktusolat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.text.Html;

public class DataProvider{
	
	private String location ;
	private String data;
	String text = null;
	String loc,date,gmt,direction,prayertime,imsak,subuh,syuruk,zohor,asar,maghrib,isyak;
	String imsakT,subuhT,syurukT,zohorT,asarT,maghribT,isyakT;
	
	public void getLoc(String loc){
		location = loc;
	}
	
	public String getData2(){
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://www.e-solat.gov.my/solat.php?kod="+location+"&lang=ENG&url=http://awislabs.com/blog");
		HttpResponse response = null;
		String html = "";
		try {
			response = client.execute(request);
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
			    str.append(line);
			}
			in.close();
			html = str.toString();
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		/*
		 * String html2 = html.replaceAll("\\<.*?>","");
		 * String html3 = html2.replaceAll("&nbsp;","");
		String html4 = html3.replaceAll("&amp;","");
		String html5 = html4.replaceAll("&#8242;","'");
		String html6 = html5.replaceAll("&#8243;","''");
		String html7 = html6.replaceAll("JAKIM","");
		 */
		
        return html;

	}
	
	
	public String controller(){		
	    data = getData2();
		return data;
	}
	
	public void convert(String html) throws IOException {
		loc = Waktusolat.realLoc;
		date = Waktusolat.getTime();
		
		String htmlString = Html.fromHtml(html).toString();			
		Pattern pattern = Pattern.compile("(\\d+:\\d+)");			
		Matcher matchcer = pattern.matcher(htmlString);			
		int pos=0;
		while(matchcer.find()){				
			switch(pos){
			case 0:					
				imsakT = matchcer.group();
				break;
			case 1:
				subuhT = matchcer.group();
				break;
			case 2:
				syurukT = matchcer.group();
				break;
			case 3:
				zohorT = matchcer.group();
				break;
			case 4:
				asarT = matchcer.group();
				break;
			case 5:
				maghribT = matchcer.group();
				break;
			case 6:
				isyakT = matchcer.group();
				break;				
			}				
			pos++;				
		}			
        
	}
	
	public String getLocation(){
		return loc;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getImsak(){
		return imsakT.trim();
	}
	
	public String getSubuh(){
		return subuhT;
	}
	
	public String getSyuruk(){
		return syurukT;
	}
	
	public String getZohor(){
		return zohorT;
	}
	
	public String getAsar(){
		return asarT;
	}
	
	public String getMaghrib(){
		return maghribT;
	}
	
	public String getIsyak(){
		return isyakT;
	}
}
