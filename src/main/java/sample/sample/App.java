package sample.sample;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class App 
{
    public static void main( String[] args )
    {
        App obj = new App();
        String forecastApi = obj.getForecastApi(args[0]);        
        obj.forecast(forecastApi);
    }
    
    public String getForecastApi(String corodinates) {
    	String url = "https://api.weather.gov/points/" + corodinates;
    	String jsonResponse = invokeApi(url);
    	JSONObject jsonData = new JSONObject(jsonResponse);
    	return jsonData.getJSONObject("properties").getString("forecast");    	
    }
    
    public String forecast(String forecastApi) {
    	String jsonResponse = invokeApi(forecastApi);
    	JSONObject jsonData = new JSONObject(jsonResponse);    	
    	JSONArray jArr = jsonData.getJSONObject("properties").getJSONArray("periods");
    	
    	for(int i =0; i<jArr.length(); i ++) {
    		JSONObject data = jArr.getJSONObject(i);
    		System.out.println(data.getString("name") + " - " + data.getString("detailedForecast"));
    	}
		return forecastApi;
    	
    }
    
    public String invokeApi(String url) {
    	String jsonResponse = "";
    	CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    	
    	try {
    		HttpGet request = new HttpGet(url);
    		request.addHeader("content-type", "application/json");
    		HttpResponse response = httpClient.execute(request);       		
    		jsonResponse = EntityUtils.toString(response.getEntity(),
					"UTF-8");
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	finally {
    		try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		return jsonResponse;
    }
}

