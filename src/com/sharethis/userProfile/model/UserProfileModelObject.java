package com.sharethis.userProfile.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileModelObject {	
	/* Sample model configuration in JSON:
    {
    	"model_init":
    	{
    		"model_name":"hour_st_os_browser_cmpn_v1",
    		"model_config_dir":"/home/webapp/rtbserver/model_up/current/res/",
    		"features":["hour","st","os", "browser", "cmpn"],
    		"model_properties":"hour_st_os_browser_cmpn_v1"
    	}
    }
	*/
	private String model_name = null;
	private String model_config_dir = null;
	private List<String> features = null;
	private String model_properties = null;
	
	public Map<String, Object> toMap() {
		Map<String, Object> dMap = new HashMap<String, Object>();
		dMap.put("model_name", model_name);
		dMap.put("model_config_dir", model_config_dir);
		dMap.put("feature", features);
		dMap.put("model_properties", model_properties);
		return dMap;		
	}
	
	public String getModel_name() {
		return model_name;
	}
	
	public String getModel_config_dir() {
		return model_config_dir;
	}
	
	public List<String> getFeature() {
		return features;
	}
	
	public String getModel_properties(){
		return model_properties;
	}
	
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	
	public void setModel_config_dir(String model_config_dir) {
		this.model_config_dir = model_config_dir;
	}
	
	public void setFeature(List<String> feature) {
		this.features = feature;
	}
	
	public void setModel_properties(String model_properties) {
		this.model_properties = model_properties;
	}
}
