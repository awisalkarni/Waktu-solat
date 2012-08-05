package com.awis.waktusolat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;

public class DataProvider {
	
//	private String location ;
//	private String data;
//	private String text = null;
	private ArrayList<HashMap<String,String>> allData = new ArrayList<HashMap<String,String>>();
	private String loc,date,gmt,direction,prayertime,imsak,subuh,syuruk,zohor,asar,maghrib,isyak;
	private String imsakT,subuhT,syurukT,zohorT,asarT,maghribT,isyakT;
	private Context context;
	
	public DataProvider(Context context){
		this.context = context;
	}
//	public void getLoc(String loc){
//		location = loc;
//	}
	
	public ArrayList<HashMap<String,String>> getDataNew(int month, String loc){
		String url = "http://www.e-solat.gov.my/prayer_time.php?zon="+loc+"&year=&jenis=3&bulan="+month+"&LG=BM";
		try {
			Document doc = Jsoup.connect(url)
					.timeout(50000)
					.get();
			Elements rows = doc.select("tr[bgcolor=#F3F2FF]");
			for (Element row : rows){
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put("tarikh", row.child(0).text());
				temp.put("hari", row.child(1).text());
				temp.put("imsak", row.child(2).text());
				temp.put("subuh", row.child(3).text());
				temp.put("syuruk", row.child(4).text());
				temp.put("zohor", row.child(5).text());
				temp.put("asar", row.child(6).text());
				temp.put("maghrib", row.child(7).text());
				temp.put("isyak", row.child(8).text());
				allData.add(temp);
			}
		} catch (IOException e) {
//			Toast.makeText(context, "Connection timeout. Sila cuba lagi", Toast.LENGTH_SHORT).show();
//			Toast toast = Toast.makeText(context, "Connection timeout. Sila cuba lagi", Toast.LENGTH_SHORT);
//			toast.show();
			e.printStackTrace();
		}
		return allData;
	}
	
//	public String getData2(){
//		HttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet("http://www.e-solat.gov.my/solat.php?kod="+location+"&lang=ENG&url=http://awislabs.com/blog");
//		HttpResponse response = null;
//		String html = "";
//		try {
//			response = client.execute(request);
//			InputStream in = response.getEntity().getContent();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//			StringBuilder str = new StringBuilder();
//			String line = null;
//			while((line = reader.readLine()) != null){
//			    str.append(line);
//			}
//			in.close();
//			html = str.toString();
//		} catch (ClientProtocolException e) {
//			
//			e.printStackTrace();
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
//		/*
//		 * String html2 = html.replaceAll("\\<.*?>","");
//		 * String html3 = html2.replaceAll("&nbsp;","");
//		String html4 = html3.replaceAll("&amp;","");
//		String html5 = html4.replaceAll("&#8242;","'");
//		String html6 = html5.replaceAll("&#8243;","''");
//		String html7 = html6.replaceAll("JAKIM","");
//		 */
//		
//        return html;
//
//	}
//	
//	
//	public String controller(){		
//	    data = getData2();
//		return data;
//	}
//	
//	public void convert(String html) throws IOException {
//		loc = Waktusolat.realLoc;
//		date = Waktusolat.getTime();
//		
//		String htmlString = Html.fromHtml(html).toString();			
//		Pattern pattern = Pattern.compile("(\\d+:\\d+)");			
//		Matcher matchcer = pattern.matcher(htmlString);			
//		int pos=0;
//		while(matchcer.find()){				
//			switch(pos){
//			case 0:					
//				imsakT = matchcer.group();
//				break;
//			case 1:
//				subuhT = matchcer.group();
//				break;
//			case 2:
//				syurukT = matchcer.group();
//				break;
//			case 3:
//				zohorT = matchcer.group();
//				break;
//			case 4:
//				asarT = matchcer.group();
//				break;
//			case 5:
//				maghribT = matchcer.group();
//				break;
//			case 6:
//				isyakT = matchcer.group();
//				break;				
//			}				
//			pos++;				
//		}			
//        
//	}
//	
//	public String getLocation(){
//		return loc;
//	}
//	
//	public String getDate(){
//		return date;
//	}
//	
//	public String getImsak(){
//		return imsakT.trim();
//	}
//	
//	public String getSubuh(){
//		return subuhT;
//	}
//	
//	public String getSyuruk(){
//		return syurukT;
//	}
//	
//	public String getZohor(){
//		return zohorT;
//	}
//	
//	public String getAsar(){
//		return asarT;
//	}
//	
//	public String getMaghrib(){
//		return maghribT;
//	}
//	
//	public String getIsyak(){
//		return isyakT;
//	}
}
