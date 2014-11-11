package com.sharethis.userProfile.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

public class ConfigurationUtil
{
	private static Logger sLogger = Logger.getLogger(ConfigurationUtil.class);
		
    public static Configuration getConfig(String resFile) throws IOException, ClassCastException {
		sLogger.info("The property file path is " + resFile + ".");
    	Configuration conf = new Configuration();
		InputStream in = new FileInputStream(new File(resFile));
		Properties props = new Properties();
		if (in != null) {
			props.load(in);
			in.close();
			Enumeration<?> enu = props.propertyNames();
			while(enu.hasMoreElements()){
				String keyName = (String) enu.nextElement();
				String valName = props.getProperty(keyName);
				sLogger.info("Key: " + keyName);
				sLogger.info("Value: " + valName);
				conf.set(keyName.trim(), valName.trim());
			}
		}
		sLogger.info("Loading the property file is done.");
		return conf;
    }
}
