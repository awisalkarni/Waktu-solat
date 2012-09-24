package com.awis.waktusolat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataProvider {
	
	private ArrayList<HashMap<String,String>> allData = new ArrayList<HashMap<String,String>>();
	
	public DataProvider(){
	}
	
	public ArrayList<HashMap<String,String>> getDataNew(int month, String loc){
		String url = "http://www.e-solat.gov.my/prayer_time.php?zon="+loc+"&year=&jenis=3&bulan="+month+"&LG=BM";
		try {
			Document doc = Jsoup.connect(url)
					.timeout(25000)
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
			e.printStackTrace();
		}
		return allData;
	}
	
}
