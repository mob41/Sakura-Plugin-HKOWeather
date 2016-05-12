package com.mob41.sakura.plugin.weather.hko;

import org.json.JSONObject;

import com.mob41.hkoweather.api.WeatherManager;
import com.mob41.hkoweather.exception.InvaildStationException;
import com.mob41.sakura.plugin.Plugin;

public class HKOWeatherPlugin extends Plugin {

	private WeatherManager weatherman;
	
	private JSONObject revData;
	
	@Override
	public void onCallPlugin() {
		try {
			weatherman = new WeatherManager();
		} catch (InvaildStationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPluginReceiveData(Object data) {
		revData = (JSONObject) data;
	}

	@Override
	public Object onPluginSendData() {
		JSONObject out = new JSONObject();
		out.put("status", 1);
		boolean ext = revData.getBoolean("ext");
		if (ext){
			String station = revData.getString("code");
			try {
				out.put("station", station);
				out.put("weather", new WeatherManager(station).getRawJSON());
				return out;
			} catch (InvaildStationException e) {
				out.put("response", "Error: " + e);
				out.put("status", -1);
				return out;
			}
		}
		try {
			weatherman.fetchWeatherReport();
		} catch (Exception e){
			out.put("response", "Error: " + e);
			out.put("status", -1);
			return out;
		}
		
		out.put("weather", weatherman.getRawJSON());
		return out;
	}

	@Override
	public void onEndPlugin() {
		
	}

}
