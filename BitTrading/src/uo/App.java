package uo;

import org.json.JSONArray;
import org.json.JSONObject;

public class App {

	public static void main(String[] args) {

		InvokeAPIs invokeApis = new InvokeAPIs();
		invokeApis.API();
		
		String input = "[{\"Ask\":699999.0,\"Bid\":660003.1,\"MarketName\":\"btc-inr\",\"Last\":700000.0},{\"Ask\":76999.0,\"Bid\":76150.0,\"MarketName\":\"eth-inr\",\"Last\":76999.0},{\"Ask\":10990.0,\"Bid\":10816.0,\"MarketName\":\"ltc-inr\",\"Last\":10700.0},{\"Ask\":1075.0,\"Bid\":1038.01,\"MarketName\":\"omg-inr\",\"Last\":1037.5},{\"Ask\":2627.0,\"Bid\":2570.0,\"MarketName\":\"qtum-inr\",\"Last\":2698.0},{\"Ask\":0.13,\"Bid\":0.1,\"MarketName\":\"eth-btc\",\"Last\":0.13},{\"Ask\":0.01749788,\"Bid\":0.01426,\"MarketName\":\"ltc-btc\",\"Last\":0.0175},{\"Ask\":0.0015999,\"Bid\":0.00141,\"MarketName\":\"omg-btc\",\"Last\":0.00141},{\"Ask\":0.0043,\"Bid\":0.00206002,\"MarketName\":\"qtum-btc\",\"Last\":0.004},{\"Ask\":74.69,\"Bid\":74.22,\"MarketName\":\"xrp-inr\",\"Last\":74.69},{\"Ask\":0.00011686,\"Bid\":0.00010638,\"MarketName\":\"xrp-btc\",\"Last\":0.0001169},{\"Ask\":101999.0,\"Bid\":100000.0,\"MarketName\":\"bch-inr\",\"Last\":105150.0}]\n" + 
				"";
		JSONArray object;
		Double value;
		object = new JSONArray(input);
		System.out.println((object).get(0));
		System.out.println(new JSONObject((object).getJSONObject(0).toString()).getDouble("Last"));
		
	}
}
