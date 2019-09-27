package com.bolsadeideas.springboot.backend.apirest.models.services.currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class CurrencylayerService {

	private static final String KEY = "d18abcc25bb6d4008756893237d9eb80";
	private static final String URL = "http://www.apilayer.net/api/live?currencies={ocurrency}&access_key={key}";

	public Double consultarApi(String ocurrency) {
		BufferedReader in;
		String linea;
		String url = URL.replace("{ocurrency}", ocurrency);
		url = url.replace("{key}", KEY);
		try {
			URL url_base = new URL(url);
			HttpsURLConnection httpcon = (HttpsURLConnection) url_base.openConnection();
			httpcon.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
			httpcon.setConnectTimeout(5000);
			httpcon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			httpcon.setRequestProperty("Accept", "application/json; charset=UTF-8");
			httpcon.setRequestMethod("GET");
			switch (httpcon.getResponseCode()) {
			case 200:
				try {
					in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
					linea = in.readLine();
					JSONObject hits = new JSONObject(linea);
					JSONArray quotes = hits.getJSONArray("quotes");
					return (Double) quotes.getDouble(0);
				} catch (IOException ioexcep) {
					System.out.println(ioexcep.getStackTrace());
				}
			case 403:
				System.out.println(httpcon.getErrorStream());
			case 404:
				System.out.println(httpcon.getErrorStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1.0;
	}

}
