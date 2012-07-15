package com.awis.waktusolat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WaktuAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<String> data;
//    private boolean time12;
    private static LayoutInflater inflater=null;
    private int mHour;
    private int mMinute;
    private String currentTime, highlightTime;
    
    public WaktuAdapter(Activity a, ArrayList<String> d) {
        activity = a;
        data = d;
//        listToHighlight = h;
//        this.time12 = time12;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initVars();
//        highlightTime = "empty";
    }
    
    @Override
    public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
    }

    
    public void initVars(){
    	final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        currentTime = new StringBuilder().append(pad(mHour)).append(":").append(pad(mMinute)).toString();
        
    }
    
    private static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}

    public int getCount() {

        return data.size();
    }

    public Object getItem(int position) {

        return position;
    }

    public long getItemId(int position) {

        return position;
    }
    
    public void setHighlight(ArrayList<String> listToHighlight, boolean time12){
    	Date currTime = null;
    	SimpleDateFormat timeFormat;
    	SimpleDateFormat timeBefore = new SimpleDateFormat("H:m");
		timeBefore.setTimeZone(TimeZone.getTimeZone("MYT"));
    	try {
    		if (time12){
    		timeFormat = new SimpleDateFormat("H:m");
//    		currentTime = timeFormat.format(timeBefore.parse(currentTime));
    	}else{
    		timeFormat = new SimpleDateFormat("H:m");
    	}
//    	SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    	timeFormat.setTimeZone(TimeZone.getTimeZone("MYT"));
//    	Calendar cal1 = Calendar.getInstance();
//    	String timeNow = timeFormat.format(cal1.getTime());
    	
    	
    	Date imsakTime = null, subuhTime = null, syurukTime = null, zohorTime = null, 
    			asarTime = null, maghribTime = null, isyakTime = null;
//    	, pukul12=null, pukul1 = null;
    	
    		currTime = (Date) timeFormat.parse(currentTime);
    		
//    		pukul12 = (Date) timeFormat.parse("11:59 PM");
//    		pukul1 = (Date) timeFormat.parse("12:00 AM");
    		imsakTime = (Date) timeFormat.parse(listToHighlight.get(0));
    		subuhTime = (Date) timeFormat.parse(listToHighlight.get(1));
    		syurukTime = (Date) timeFormat.parse(listToHighlight.get(2));
    		zohorTime = (Date) timeFormat.parse(listToHighlight.get(3));
    		asarTime = (Date) timeFormat.parse(listToHighlight.get(4));
    		maghribTime = (Date) timeFormat.parse(listToHighlight.get(5));
    		isyakTime = (Date) timeFormat.parse(listToHighlight.get(6));
//    		highlightTime = "empty";
    		if(currTime.equals(imsakTime) || (currTime.after(imsakTime) && currTime.before(subuhTime))){
    			highlightTime = "Imsak";
    		}
    		else{
//    			highlightTime = "empty";
    		}
    		if(currTime.equals(subuhTime) || (currTime.after(subuhTime) && currTime.before(syurukTime))){
    			highlightTime = "Subuh";
    		}
    		else{
//    			highlightTime = "empty";
    		}
    		if(currTime.equals(syurukTime) || (currTime.after(syurukTime) && currTime.before(zohorTime))){
    			highlightTime = "Syuruk";
    		}
    		else{
//    			highlightTime = "empty";
    		}
    		if(currTime.equals(zohorTime) || (currTime.after(zohorTime) && currTime.before(asarTime))){
    			highlightTime = "Zohor";
    		}
    		else{
//    			highlightTime = "empty";
    		}
    		if(currTime.equals(asarTime) || (currTime.after(asarTime) && currTime.before(maghribTime))){
    			highlightTime = "Asar";
    		}
    		else{
//    			highlightTime = "empty";
    		}
    		if(currTime.equals(maghribTime) || (currTime.after(maghribTime) && currTime.before(isyakTime))){
    			highlightTime = "Maghrib";
    		}
    		else{
//    			highlightTime = "empty";
    		}
    		if(currTime.equals(isyakTime) || currTime.after(isyakTime) || currTime.before(imsakTime)){
    			highlightTime = "Isyak";
    		}
    		else{
//    			highlightTime = "empty";
    		}
    	//location.append(currTime.toLocaleString());
//    	if (currTime.compareTo(subuhTime)==1 && currTime.compareTo(syurukTime)==-1){
//    		highlightTime = "Subuh";
//    	}else if (currTime.compareTo(zohorTime)==1 && currTime.compareTo(asarTime)==-1){
//    		highlightTime = "Zohor";
//    	}else if (currTime.compareTo(asarTime)==1 && currTime.compareTo(maghribTime)==-1){
//    		highlightTime = "Asar";
//    	}else if (currTime.compareTo(maghribTime)==1 && currTime.compareTo(isyakTime)==-1){
//    		highlightTime = "Maghrib";
//    	}else if (currTime.compareTo(isyakTime)==1 && currTime.compareTo(pukul12)==-1){
//    		highlightTime = "Isyak";
//    	}else if (currTime.compareTo(pukul1)==1 && currTime.compareTo(imsakTime)==-1){
//    		highlightTime = "Isyak";
//    	}else if (currTime.compareTo(imsakTime)==1 && currTime.compareTo(subuhTime)==-1){
//    		highlightTime = "Imsak";
//    	}
    	
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}catch (NullPointerException e) {
    		e.printStackTrace();
    	}
//    	Log.e("Highlight", highlightTime);
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null) vi = inflater.inflate(R.layout.list_layout, null);
//        Log.e("Listing", highlightTime);
        TextView waktuText=(TextView)vi.findViewById(R.id.txtItem);
//        TextView Contenttext=(TextView)vi.findViewById(R.id.txtContent);
//        highlightTime = "Isyak";
        waktuText.setText(data.get(position));
//        Log.e("Listing", data.get(position));
        try {
        	if (data.get(position).contains(highlightTime)){
        		
            	waktuText.setTextColor(Color.GREEN);
            }else {
            	waktuText.setTextColor(Color.WHITE);
            }
        }catch (Exception e){
        	e.printStackTrace();
        }
        
//        Contenttext.setText(data.get(position).get("desc").toString());
//        ImageView image=(ImageView)vi.findViewById(R.id.imgViewer2);
//        if (data.get(position).get("img").toString().equals("noImg")!=true){
//        	imageLoader.DisplayImage(data.get(position).get("img").toString(), image);
//        }else {
//        	imageLoader.DisplayImageNull(data.get(position).get("img").toString(), image);
//        	imageLoader.DisplayImage("http://amanz.my/wp-content/uploads/2011/10/logo78x78.png", image);
        	
//        }

        return vi;
    }
}