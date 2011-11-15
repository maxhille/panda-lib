package com.lambdasoup.panda;

/**
 * @author <a href="mailto: vivien@new-bamboo.co.uk">Vivien Schilis</a>
 * @author Vivien Schilis
 */

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class PandaHttp {
	public enum Method {
		GET, POST, UPDATE, DELETE
	}

	static String get(String url, Map<String, String> params, Properties properties) {
		Map<String, String> sParams = signedParams("GET", url, params, properties);
		String flattenParams = canonicalQueryString(sParams);
		String requestUrl = "http://" + properties.getProperty("api-host") + ":80/v2" + url + "?" + flattenParams;
		HttpGet httpGet = new HttpGet(requestUrl);
		DefaultHttpClient httpclient = new DefaultHttpClient();

		String stringResponse = null;

		try {
			HttpResponse response = httpclient.execute(httpGet);
			stringResponse = EntityUtils.toString(response.getEntity());
		} catch(IOException e){
			e.printStackTrace();
		}

		return stringResponse;
	}


	static String post(String url, Map<String,String> params, Properties properties) {
		Map<String,String> sParams = signedParams("POST", url, params, properties);
		String flattenParams = canonicalQueryString(sParams);
		String requestUrl = "http://" + properties.getProperty("api-host") + ":80/v2" + url + "?" + flattenParams;

		HttpPost httpPost = new HttpPost(requestUrl);
		DefaultHttpClient httpclient = new DefaultHttpClient();

		String stringResponse = null;

		try {
			//httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			//httpPost.setEntity(new StringEntity(canonicalQueryString(sParams), "UTF-8"));
			HttpResponse response = httpclient.execute(httpPost);

			stringResponse = EntityUtils.toString(response.getEntity());

		} catch(IOException e){
			e.printStackTrace();
		}

		return stringResponse;
	}

	/*	
	public String put(String url, TreeMap params) {
		TreeMap sParams = signedParams("PUT", url, params);
		return "";
	}


	public String delete(String url, TreeMap params) {
		TreeMap sParams = signedParams("DELETE", url, params);
		return "";
	}
	 */	

	static Map<String, String> signedParams(String method, String url, Map<String, String> params, Properties properties) {
		params.put("cloud_id", properties.getProperty("cloud-id"));
		params.put("access_key", properties.getProperty("access-key"));
		params.put("timestamp", new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" ).format(new Date()));
		//		params.put("api_host", properties.getProperty("api-host"));
		params.put("signature", generateSignature(method, url, properties.getProperty("api-host"), properties.getProperty("secret-key"), params));
		return params;
	}

	private static String generateSignature(String method, String url, String host, String secretKey, Map<String,String> params) {
		String queryString = canonicalQueryString(params);
		String stringToSign = method.toUpperCase() + "\n" + host + "\n" + url + "\n" + queryString;

		String signature = null;

		try {

			SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);

			byte[] rawHmac = mac.doFinal(stringToSign.getBytes());

			signature = new String(Base64.encodeBase64(rawHmac));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		return signature;
	}

	private static String canonicalQueryString(Map<String,String> map) {
		List<BasicNameValuePair> qparams = new ArrayList<BasicNameValuePair>();

		for(Map.Entry<String, String> entry : map.entrySet()) {
			qparams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));	
		}

		Comparator<BasicNameValuePair> comparator = new Comparator<BasicNameValuePair>() {
			public int compare(BasicNameValuePair o1, BasicNameValuePair o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};

		Collections.sort(qparams, comparator);

		return URLEncodedUtils.format(qparams, "UTF-8");
	}


	public static void delete(String url, HashMap<String, String> params, Properties properties) {
		Map<String, String> sParams = signedParams("DELETE", url, params, properties);
		String flattenParams = canonicalQueryString(sParams);
		String requestUrl = "http://" + properties.getProperty("api-host") + ":80/v2" + url + "?" + flattenParams;
		HttpDelete httpDelete = new HttpDelete(requestUrl);
		DefaultHttpClient httpclient = new DefaultHttpClient();

		try {
			httpclient.execute(httpDelete);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
