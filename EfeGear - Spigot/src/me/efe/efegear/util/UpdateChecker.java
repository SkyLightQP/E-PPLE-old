package me.efe.efegear.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
	public static double getVersion(String name) {
		try {
			URL url = new URL("https://raw.githubusercontent.com/cwjh1002/Update-Checker/master/README.md");
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				if (inputLine.contains("%"+name+"%")) {
					String version = inputLine.split("%"+name+"%")[1];
					return Double.parseDouble(version);
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println("UpdateChecker Exception: " + e.toString());
		}
		return 0;
	}
}