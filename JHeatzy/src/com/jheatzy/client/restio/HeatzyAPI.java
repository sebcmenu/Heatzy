package com.jheatzy.client.restio;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jheatzy.client.Device;
import com.jheatzy.client.HeatingMode;
import com.jheatzy.client.schedule.WeekSchedule;

public class HeatzyAPI {
	private final static String BASEURL = "https://euapi.gizwits.com/";
	private final static String APPID = "c70a66ff039d41b4a220e198b0fcc8b3";
	private final static String APPID_H = "X-Gizwits-Application-Id";
	private final static String TOKEN_H = "X-Gizwits-User-token";

	private HttpClientContext context;
	private TokenProvider tp;
	private CloseableHttpClient hc;

	public HeatzyAPI(TokenProvider tp) {
		context = HttpClientContext.create();
		BasicCookieStore st = new BasicCookieStore();
		context.setCookieStore(st);

		RequestConfig cfg = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build();

		hc = HttpClients.custom().setDefaultRequestConfig(cfg).setRedirectStrategy(new LaxRedirectStrategy()).build();

		this.tp = tp;

	}

	public String connect(String login, String password) throws Exception {

		JSONObject jso = new JSONObject();
		jso.put("username", login);
		jso.put("password", password);

		JSONObject resp = requestJS("app/login", jso, false);

		return resp.getString("token");
	}

	public List<Device> listDevices() throws Exception {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("limit", "20"));
		params.add(new BasicNameValuePair("skip", "0"));

		List<Device> result = new ArrayList<Device>();

		JSONObject resp = requestGetJS("app/bindings", params, true);
		JSONArray devices = resp.getJSONArray("devices");

		devices.forEach(t -> {
			JSONObject jso = (JSONObject) t;
			Device d = new Device(this,jso.getString("did"));
			d.setAlias(jso.getString("dev_alias"));
			result.add(d);
		});

		return result;
	}
	
	public void changeMode(Device d, HeatingMode m) throws Exception {

		JSONObject jso2 = new JSONObject();
		jso2.put("mode", m.getValue());
		JSONObject jso = new JSONObject();
		jso.put("attrs", jso2);

		requestJS("app/control/"+d.getId(), jso, true);

	}
	
	public WeekSchedule getDeviceSchedule(Device device) throws Exception{
	
		JSONObject resp = requestGetJS("app/devdata/"+device.getId()+"/latest", null, true);
		JSONObject jso = (JSONObject) resp.get("attr");
		
		WeekSchedule ws=new WeekSchedule();
		JSONHelper.fillScheduleFromJson(jso, ws,HeatingMode.ECO);
		
		return ws;
	}
	
	
	private JSONObject requestJS(String uri, JSONObject jso, boolean addToken)
			throws ClientProtocolException, IOException {

		HttpPost postRequest = new HttpPost(BASEURL + uri);
		StringEntity entity = new StringEntity(jso.toString());
		postRequest.setEntity(entity);

		return executeRequest(addToken, postRequest);
	}

	private JSONObject requestGetJS(String uri, List<NameValuePair> params, boolean addToken)
			throws ClientProtocolException, IOException, URISyntaxException {

		URIBuilder address = new URIBuilder(BASEURL + uri);
		if (params!=null)
			address.setParameters(params);
		HttpGet getRequest = new HttpGet(address.build());

		return executeRequest(addToken, getRequest);
	}

	private JSONObject executeRequest(boolean addToken, HttpUriRequest request)
			throws IOException, ClientProtocolException {
		CloseableHttpResponse response;
		request.addHeader(APPID_H, APPID);
		if (addToken)
			request.addHeader(TOKEN_H, tp.getToken());

		response = hc.execute(request, context);
		try {
			HttpEntity e = response.getEntity();

			String body = new String(IOUtils.toByteArray(e.getContent()));
			System.err.println(body);
			JSONObject answer = new JSONObject(body);
			return answer;

		} finally {
			response.close();
		}
	}





}
