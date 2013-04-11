package com.vaggs.Utils;

import java.util.Map;

import com.google.appengine.labs.repackaged.com.google.common.collect.Maps;

public class Constants {

	public static String MAPS_API_KEY = "AIzaSyAK0HG-UZtFAiPtHkyc6xjxI2wQXCOX4Pk";
	
	public enum CHANNEL_MODE {
		TOWER("tower"),
		COCKPIT("cockpit");
		
		private String name;
		
		CHANNEL_MODE(String name) {
			this.name = name;
		}
		
		String getName() {
			return name;
		}
		
		private static Map<String, CHANNEL_MODE> nameMap = Maps.newHashMap();
		static {
			for(CHANNEL_MODE o : CHANNEL_MODE.values()) {
				nameMap.put(o.name, o);
			}
		}
		
		public static CHANNEL_MODE getMode(String name) {
			return nameMap.get(name);
		}
		
	}

}
