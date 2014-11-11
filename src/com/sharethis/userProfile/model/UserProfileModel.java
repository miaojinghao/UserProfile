package com.sharethis.userProfile.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sharethis.common.ad.EnumFeature;
import com.sharethis.common.ad.ModelInterface;
import com.sharethis.common.error.ErrorCode;
import com.sharethis.common.helper.log.LogHelper;
import com.sharethis.userProfile.model.ConfigurationUtil;

public class UserProfileModel implements ModelInterface<String []> {
	public static String PROPERTIES_FILE = "up_pred.properties";
	private int modelId = 0;
	private List<String> fList = new ArrayList<String>(0);
	private List<EnumFeature> lEnumF = new ArrayList<EnumFeature>(0);
	private double[] modelCoeffs;
	private UserProfileModelInterface model = null;
	private static final Logger log = Logger.getLogger(UserProfileModel.class);
	
	/* Set Model ID */
	public void setModelId(int id) {
		this.modelId = id;
	}
	
	/* Get Model ID */
	public int getModelId() {
		return this.modelId;
	}
	
	/* Model initialization */
	public boolean init(String jsonModelInit) throws Exception {
		if (jsonModelInit != null && !("null".equalsIgnoreCase(jsonModelInit)) && !(jsonModelInit.isEmpty())) {
			/* Prepare model features */
			log.info("Load model properties ...");
			Gson gson = new Gson();
			UserProfileModelObject obj = new UserProfileModelObject();
			obj = gson.fromJson(jsonModelInit, UserProfileModelObject.class);
			String modelName = (String) obj.getModel_name();
			List<String> featureList = (List<String>) obj.getFeature();
			String model_config_dir = (String) obj.getModel_config_dir();
			String modelProperty = (String) obj.getModel_properties();
			boolean ret = getModelInfo(modelName, featureList, model_config_dir, modelProperty);
			if (!ret)
				return ret;
			log.info("Model properties loaded.");
			
			/* Instantiate model */
			log.info("Instantiate model interface ...");
			model = new UserProfileModelInterface();
	        log.info("Model interface instantiated.");
			return true;
		}
		else {
			log.error("Model config string in JSON [" + jsonModelInit + "] is null or empty!");
			return false;
		}
	}
	
	private boolean getModelInfo(String modelName, List<String> featureList, String model_config_dir, String model_prop) throws Exception {
		try {
			log.info("Model Name: " + modelName);
			Configuration config = ConfigurationUtil.getConfig(model_config_dir + "/" + PROPERTIES_FILE);
			String model_params = config.get(model_prop);
			String[] tokens = StringUtils.split(model_params, "|");
			HashMap<String, Double>coeffMap = new HashMap<String, Double>();
			for (int i = 0; i < tokens.length; i++) {
				String[] f = tokens[i].split(":");
				if (f.length == 2)
					coeffMap.put(f[0], Double.parseDouble(f[1]));
				else if (f.length == 1)
					coeffMap.put(f[0], new Double(0.0f));
			}
			log.info("Model Features:");
			modelCoeffs = new double[featureList.size()];
			for(int i = 0; i < featureList.size(); i++) {
				log.info(featureList.get(i));
				modelCoeffs[i] = (double) coeffMap.get(featureList.get(i));
				fList.add(i, featureList.get(i));
				EnumFeature enumF = EnumFeature.getEnum(featureList.get(i));
				lEnumF.add(i, enumF);
			}
		} catch (Exception e) {
			log.error("Error in loading model " + modelName + "\n" + LogHelper.formatMessage(e));
			return false;
		}
		return true;
	}
	
	/* Get model features */
	public List<String> getFeatures() {
		return fList;
	}
	
	/* Evaluation */
	public float evaluate(String[] featureValues) {
		int nCols = featureValues.length;
		
		/* Pre-process the input feature values: browser & os */
		String[] inputs = new String[model.getNumCols()];
		int i = 0;
		for (; i < nCols; i++) {
			if (featureValues[i].isEmpty() || featureValues[i].equalsIgnoreCase("unknown") || featureValues[i].equalsIgnoreCase("null"))
				featureValues[i] = "-";
			if (fList.get(i) != null && fList.get(i).equals("hour")) {
				inputs[i] = UserProfileModelData.HM_HOUR_GROUP.get(featureValues[i]);
				if (inputs[i] == null)
					inputs[i] = "1";
			}
			else if (fList.get(i) != null && fList.get(i).equals("browser"))
				inputs[i] = featureValues[i].replaceAll("[^a-zA-Z]", "");
			else if (fList.get(i) != null && fList.get(i).equals("os"))
				inputs[i] = featureValues[i].replaceAll("\\s|(\\(.+?\\))|\\.[a-zA-Z0-9]+?", "");
			else
				inputs[i] = featureValues[i];
		}	
		for (; i < model.getNumCols(); i++)
			inputs[i] = "-";
		
    	return model.predict(inputs);
	}
	
	public List<EnumFeature> getFeatureEnums() {		
		return lEnumF;
	}
	
	public static void main(String[] args) {
		int iReturn = ErrorCode.ERROR_GENERAL;
		if (args.length < 2) {
			System.out.println("Usage: java -cp jar_file com.sharethis.userProfile.model.UserProfileModel inputfile, outputfile");
			System.exit(iReturn);
		}
		try {
			String modelName = "hour_st_os_browser_cmpn_v1";
			String modelFeature="hour,st,os,browser,cmpn";
			List<String> featureList = new ArrayList<String>();
			String[] keyNames = StringUtils.split(modelFeature, ",");
			for(int i = 0; i < keyNames.length; i++)
				featureList.add(i, keyNames[i]);
			
			UserProfileModelObject obj = new UserProfileModelObject();
			obj.setModel_name(modelName);
			obj.setFeature(featureList);
			obj.setModel_config_dir("res");
			obj.setModel_properties(modelName);
			Gson gson = new Gson();
			String model_json_obj = gson.toJson(obj);
			
			log.info("Initiate UserProfile Model ...");
			log.info(model_json_obj);
			UserProfileModel model = new UserProfileModel();
			if(!model.init(model_json_obj)) {
				log.error("Failed in initializing User Profile Model. System is exiting...");
				iReturn = ErrorCode.ERROR_GENERAL;
				System.exit(iReturn);
			}
			
			/* Read input file */
			String inputFileName = args[0];
			String outputFileName = args[1];
			int lineno = 0;
			long ns = 0;
			BufferedReader input = new BufferedReader(new FileReader(inputFileName));
	        BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName));
	        String line = null;
	        while ((line = input.readLine()) != null) {
	        	line = line.trim();
	        	String[] features = line.split("\t");
	        	String[] inputs = new String[features.length - 2];
	        	for (int j = 1; j < features.length - 1; j++) {
	        		inputs[j - 1] = features[j].trim();
	        	}
	        	long start_ns = System.nanoTime();
	        	float result = model.evaluate(inputs);
	        	long end_ns = System.nanoTime();
	        	// log.info(lineno + ": " + Arrays.toString(inputs) + "\t" + String.format("%.6f", result));
	        	ns += (end_ns - start_ns);
	        	lineno++;
	        	output.write(line + "\t" + String.format("%.6f", result) + "\n");
	        }
	        input.close();
	        output.close();
	        log.info("Total number of records: " + lineno);
	        log.info("Total number of milliseconds: " + ns / 1e6);
	        log.info("Total memory usage: " + ((double) Runtime.getRuntime().totalMemory()) / (1024.0 * 1024.0));
		} catch(Exception e) {
			log.error("Error: " + e.toString());
			iReturn = ErrorCode.ERROR_GENERAL;
		}		
		System.exit(iReturn);
	}
}