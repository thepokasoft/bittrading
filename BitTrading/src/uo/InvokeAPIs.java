package uo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InvokeAPIs {

	private static final InvokeGoogleSheets invokeGoogleSheets = InvokeGoogleSheets.getInstance();

	public String API() {

		// USD -> INR Convertion Rate
		String fixerAPI = "https://api.fixer.io/latest?base=USD&symbols=INR";
		// Cex
		String cexEthAPI = "https://cex.io/api/last_price/ETH/USD";
		String cexBtcAPI = "https://cex.io/api/last_price/BTC/USD";
		String cexXrpAPI = "https://cex.io/api/last_price/XRP/USD";
		// Koinex
		String koinexAPI = "https://koinex.in/api/ticker";
		// CoinDelta
		String coinDeltaAPI = "https://api.coindelta.com/api/v1/public/getticker/";
		// Bittrex
		String bittrexAPI = "https://bittrex.com/api/v1.1/public/getcurrencies";

		try {

			OkHttpClient client = new OkHttpClient();
			Request request;
			Response response;
			JSONObject object;
			Map<String, Double> sheetValuesMap = new HashMap<String, Double>();
			Double value;
			// Fixer
			request = new Request.Builder().url(fixerAPI).build();
			response = client.newCall(request).execute();
			object = new JSONObject(response.body().string());
			//Double INR = object.getJSONObject("rates").getDouble("INR");
			Double INR = 1.0;
			// Cex Eth
			request = new Request.Builder().url(cexEthAPI).build();
			response = client.newCall(request).execute();
			object = new JSONObject(response.body().string());
			value = Double.parseDouble(object.getString("lprice").toString()) * INR;
			sheetValuesMap.put("Sheet1!B3", value);
			// Cex BTC
			request = new Request.Builder().url(cexBtcAPI).build();
			response = client.newCall(request).execute();
			object = new JSONObject(response.body().string());
			value = Double.parseDouble(object.getString("lprice").toString()) * INR;
			sheetValuesMap.put("Sheet1!C3", value);
			// Cex Xrp
			request = new Request.Builder().url(cexXrpAPI).build();
			response = client.newCall(request).execute();
			object = new JSONObject(response.body().string());
			value = Double.parseDouble(object.getString("lprice").toString()) * INR;
			sheetValuesMap.put("Sheet1!D3", value);
			// Koinex
			request = new Request.Builder().url(koinexAPI).build();
			response = client.newCall(request).execute();
			object = new JSONObject(response.body().string());
			// Eth
			value = Double.parseDouble(object.getJSONObject("prices").getJSONObject("inr").getString("ETH")) * INR;
			sheetValuesMap.put("Sheet1!B4", value);
			// BTC
			value = Double.parseDouble(object.getJSONObject("prices").getJSONObject("inr").getString("BTC")) * INR;
			sheetValuesMap.put("Sheet1!C4", value);
			// XRP
			value = Double.parseDouble(object.getJSONObject("prices").getJSONObject("inr").getString("XRP")) * INR;
			sheetValuesMap.put("Sheet1!D4", value);

			// CoinDelta
			request = new Request.Builder().url(coinDeltaAPI).build();
			response = client.newCall(request).execute();
			JSONArray responseArray = new JSONArray(response.body().string());
			// ETH
			object = new JSONObject(responseArray.getJSONObject(0).toString());
			value = (object.getDouble("Last")) * INR;
			sheetValuesMap.put("Sheet1!B5", value);
			// BTC
			object = new JSONObject(responseArray.getJSONObject(1).toString());
			//object = new JSONObject((new JSONArray(response.body().string())).getJSONObject(1).toString());
			value = (object.getDouble("Last")) * INR;
			sheetValuesMap.put("Sheet1!C5", value);
			// XRP
			object = new JSONObject(responseArray.getJSONObject(0).toString());
			//object = new JSONObject((new JSONArray(response.body().string())).getJSONObject(9).toString());
			value = (object.getDouble("Last")) * INR;
			sheetValuesMap.put("Sheet1!C5", value);
			
			System.out.println("Printing SheetValuesMap");
			System.out.println(sheetValuesMap);
			invokeGoogleSheets.updateSheet(sheetValuesMap);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return cexEthAPI;

	}

	public Response getResponse(String URL) throws IOException {
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url(URL).build();

		Response response = client.newCall(request).execute();

		return response;

	}
}
// https://github.com/stleary/JSON-java