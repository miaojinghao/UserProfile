package com.sharethis.userProfile.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;

public class UserProfileModelData extends Thread {
	private static final String model_file = "data/model.txt";
	private static final String model_status_file = "data/_SUCCESS";
	private static final Logger log = Logger.getLogger(UserProfileModelData.class);
	
	/* Features in an array of string arrays*/
	private static String[][] FEATURES = new String[5][];
	static {
		/* Feature 0: HOUR */
		FEATURES[0] = new String[4];
		FEATURES[0][0] = "1";
		FEATURES[0][1] = "2";
		FEATURES[0][2] = "3";
		FEATURES[0][3] = "4";
	
		/* Feature 1: ST: State */
		FEATURES[1] = new String[55];
		FEATURES[1][0] = "-";
		FEATURES[1][1] = "AA";
		FEATURES[1][2] = "AE";
		FEATURES[1][3] = "AK";
		FEATURES[1][4] = "AL";
		FEATURES[1][5] = "AP";
		FEATURES[1][6] = "AR";
		FEATURES[1][7] = "AZ";
		FEATURES[1][8] = "CA";
		FEATURES[1][9] = "CO";
		FEATURES[1][10] = "CT";
		FEATURES[1][11] = "DC";
		FEATURES[1][12] = "DE";
		FEATURES[1][13] = "FL";
		FEATURES[1][14] = "GA";
		FEATURES[1][15] = "HI";
		FEATURES[1][16] = "IA";
		FEATURES[1][17] = "ID";
		FEATURES[1][18] = "IL";
		FEATURES[1][19] = "IN";
		FEATURES[1][20] = "KS";
		FEATURES[1][21] = "KY";
		FEATURES[1][22] = "LA";
		FEATURES[1][23] = "MA";
		FEATURES[1][24] = "MD";
		FEATURES[1][25] = "ME";
		FEATURES[1][26] = "MI";
		FEATURES[1][27] = "MN";
		FEATURES[1][28] = "MO";
		FEATURES[1][29] = "MS";
		FEATURES[1][30] = "MT";
		FEATURES[1][31] = "NC";
		FEATURES[1][32] = "ND";
		FEATURES[1][33] = "NE";
		FEATURES[1][34] = "NH";
		FEATURES[1][35] = "NJ";
		FEATURES[1][36] = "NM";
		FEATURES[1][37] = "NV";
		FEATURES[1][38] = "NY";
		FEATURES[1][39] = "OH";
		FEATURES[1][40] = "OK";
		FEATURES[1][41] = "OR";
		FEATURES[1][42] = "PA";
		FEATURES[1][43] = "RI";
		FEATURES[1][44] = "SC";
		FEATURES[1][45] = "SD";
		FEATURES[1][46] = "TN";
		FEATURES[1][47] = "TX";
		FEATURES[1][48] = "UT";
		FEATURES[1][49] = "VA";
		FEATURES[1][50] = "VT";
		FEATURES[1][51] = "WA";
		FEATURES[1][52] = "WI";
		FEATURES[1][53] = "WV";
		FEATURES[1][54] = "WY";
	
		/* Feature 2: OS */
		FEATURES[2] = new String[29];
		FEATURES[2][0] = "-";
		FEATURES[2][1] = "Android";
		FEATURES[2][2] = "Android1";
		FEATURES[2][3] = "Android2";
		FEATURES[2][4] = "Android2Tablet";
		FEATURES[2][5] = "Android3Tablet";
		FEATURES[2][6] = "Android4";
		FEATURES[2][7] = "Android4Tablet";
		FEATURES[2][8] = "BlackBerryOS";
		FEATURES[2][9] = "BlackBerryTabletOS";
		FEATURES[2][10] = "iOS4";
		FEATURES[2][11] = "iOS5";
		FEATURES[2][12] = "iOS6";
		FEATURES[2][13] = "Linux";
		FEATURES[2][14] = "MacOS";
		FEATURES[2][15] = "MacOSX";
		FEATURES[2][16] = "Maemo";
		FEATURES[2][17] = "NintendoWii";
		FEATURES[2][18] = "SonyPlaystation";
		FEATURES[2][19] = "SunOS";
		FEATURES[2][20] = "Windows";
		FEATURES[2][21] = "Windows2000";
		FEATURES[2][22] = "Windows7";
		FEATURES[2][23] = "Windows8";
		FEATURES[2][24] = "Windows98";
		FEATURES[2][25] = "WindowsPhone7";
		FEATURES[2][26] = "WindowsPhone8";
		FEATURES[2][27] = "WindowsVista";
		FEATURES[2][28] = "WindowsXP";
	
		/* Feature 3: BROWSER */
		FEATURES[3] = new String[25];
		FEATURES[3][0] = "-";
		FEATURES[3][1] = "AdobeAIRapplication";
		FEATURES[3][2] = "AppleWebKit";
		FEATURES[3][3] = "Camino";
		FEATURES[3][4] = "CFNetwork";
		FEATURES[3][5] = "Chrome";
		FEATURES[3][6] = "ChromeMobile";
		FEATURES[3][7] = "DownloadingTool";
		FEATURES[3][8] = "Firefox";
		FEATURES[3][9] = "Flock";
		FEATURES[3][10] = "IEMobile";
		FEATURES[3][11] = "InternetExplorer";
		FEATURES[3][12] = "iTunes";
		FEATURES[3][13] = "Konqueror";
		FEATURES[3][14] = "Lynx";
		FEATURES[3][15] = "MobileSafari";
		FEATURES[3][16] = "Mozilla";
		FEATURES[3][17] = "Omniweb";
		FEATURES[3][18] = "Opera";
		FEATURES[3][19] = "OperaMini";
		FEATURES[3][20] = "RobotSpider";
		FEATURES[3][21] = "Safari";
		FEATURES[3][22] = "SeaMonkey";
		FEATURES[3][23] = "Silk";
		FEATURES[3][24] = "Thunderbird";

		/* Feature 4: CMPN: CAMPAIGNID */
		FEATURES[4] = new String[28];
		FEATURES[4][0] = "-";
		FEATURES[4][1] = "1000001";
		FEATURES[4][2] = "1000031";
		FEATURES[4][3] = "1000037";
		FEATURES[4][4] = "1000120";
		FEATURES[4][5] = "1000121";
		FEATURES[4][6] = "1000129";
		FEATURES[4][7] = "1000139";
		FEATURES[4][8] = "1000140";
		FEATURES[4][9] = "1000150";
		FEATURES[4][10] = "1000217";
		FEATURES[4][11] = "1000218";
		FEATURES[4][12] = "1000245";
		FEATURES[4][13] = "1000258";
		FEATURES[4][14] = "1000259";
		FEATURES[4][15] = "1000265";
		FEATURES[4][16] = "1000266";
		FEATURES[4][17] = "1000299";
		FEATURES[4][18] = "1000314";
		FEATURES[4][19] = "1000342";
		FEATURES[4][20] = "1000343";
		FEATURES[4][21] = "1000346";
		FEATURES[4][22] = "196354500";
		FEATURES[4][23] = "197082660";
		FEATURES[4][24] = "198215820";
		FEATURES[4][25] = "198681780";
		FEATURES[4][26] = "198682020";
		FEATURES[4][27] = "4250";
	};
	
	public static final HashMap<String, String> HM_HOUR_GROUP;
	static {
		HM_HOUR_GROUP = new HashMap<String, String>();
		HM_HOUR_GROUP.put("07", "1");
		HM_HOUR_GROUP.put("08", "1");
		HM_HOUR_GROUP.put("09", "1");
		HM_HOUR_GROUP.put("10", "1");
		HM_HOUR_GROUP.put("11", "1");
		HM_HOUR_GROUP.put("12", "1");
		HM_HOUR_GROUP.put("13", "1");
		HM_HOUR_GROUP.put("14", "2");
		HM_HOUR_GROUP.put("15", "2");
		HM_HOUR_GROUP.put("16", "2");
		HM_HOUR_GROUP.put("17", "2");
		HM_HOUR_GROUP.put("18", "3");
		HM_HOUR_GROUP.put("19", "3");
		HM_HOUR_GROUP.put("20", "3");
		HM_HOUR_GROUP.put("21", "3");
		HM_HOUR_GROUP.put("22", "3");
		HM_HOUR_GROUP.put("23", "3");
		HM_HOUR_GROUP.put("00", "3");
		HM_HOUR_GROUP.put("01", "4");
		HM_HOUR_GROUP.put("02", "4");
		HM_HOUR_GROUP.put("03", "4");
		HM_HOUR_GROUP.put("04", "4");
		HM_HOUR_GROUP.put("05", "4");
		HM_HOUR_GROUP.put("06", "4");
	};
	
	private AtomicReference<HashMap<ByteBuffer, Double>> HM_MODEL_REF_USE;
	private AtomicLong model_file_last_modified;	// Model last modified time stamp
	private Timer timer;	// For periodically upload new model file
	
	private void loadModel(boolean forced) {
		File f_model_status = new File(model_status_file);
		if (!f_model_status.exists() || !f_model_status.isFile()) {
			log.error("Model status file " + model_status_file + " is not ready.");
			return;
		}
		
		File f_model = new File(model_file);
		if (f_model.length() == 0) {
			log.error("Model file " + model_file + " is empty.");
			return;
		}
		else {
			if (!forced && f_model.lastModified() == model_file_last_modified.get()) {
				log.warn("Model file " + model_file + " is not updated. Will not update the model.");
				return;
			}
			else {
				log.warn("Model file " + model_file + " updated (old: " + model_file_last_modified.get() + " | new: " + f_model.lastModified() + ")");
				model_file_last_modified.set(f_model.lastModified());
			}
		}
		
		HashMap<ByteBuffer, Double> HM_MODEL_DATA = new HashMap<ByteBuffer, Double>();
		try {
			BufferedReader input = new BufferedReader(new FileReader(model_file));
			String line = null;
			while ((line = input.readLine()) != null) {
				line = line.trim();
				String[] tokens = line.split("\t");
				if (tokens.length >= 7) {
					String key = tokens[0] + "\t" + tokens[1] + "\t" + tokens[2] + "\t" + tokens[3] + "\t" + tokens[4];
					double imp = Double.parseDouble(tokens[5]);
					double conv = Double.parseDouble(tokens[6]);
					double ctr = imp > 0 ? (conv + 0.1) / (imp + 1000.0) : 0.0;
					byte[] b_key = DigestUtils.md5(key);
					HM_MODEL_DATA.put(ByteBuffer.wrap(b_key), new Double(ctr));
				}
			}
			input.close();
			HM_MODEL_REF_USE.set(HM_MODEL_DATA);
			log.info("Model loaded from file: " + model_file);
		} catch (Exception e) {
			log.error("Cannot open model file: " + model_file);
		}
	}
	
	public UserProfileModelData () {
		HM_MODEL_REF_USE = new AtomicReference<HashMap<ByteBuffer, Double>>(new HashMap<ByteBuffer, Double>());
		model_file_last_modified = new AtomicLong(0);
		timer = new Timer();
		loadModel(true);
	}
	
	protected void finalize() throws Throwable {
		timer.purge();
		timer.cancel();
	}
	
	public void run() {
		// Periodically check the model file
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				loadModel(false);
			}
		};
		if (task != null)
			timer.schedule(task, 2000, 2000);
	}
	
	/* Get feature values*/
	public String[][] getDomainValues() {
		return FEATURES;
	}
	
	public boolean has_key(String key) {
		byte[] b_key = DigestUtils.md5(key);
		return HM_MODEL_REF_USE.get().containsKey(ByteBuffer.wrap(b_key));
	}
	
	public float get_score(String key) {
		byte[] b_key = DigestUtils.md5(key);
		if (HM_MODEL_REF_USE.get().containsKey(ByteBuffer.wrap(b_key)))
			return HM_MODEL_REF_USE.get().get(ByteBuffer.wrap(b_key)).floatValue();
		else
			return 0.0f;
	}
}