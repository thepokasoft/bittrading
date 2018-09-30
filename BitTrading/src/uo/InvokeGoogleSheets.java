package uo;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Update;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvokeGoogleSheets {
	/** Application name. */
	private static final String APPLICATION_NAME = "Bit Trading";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/sheets.googleapis.com-Bit-Trading");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/sheets.googleapis.com-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = InvokeGoogleSheets.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Sheets API client service.
	 * 
	 * @return an authorized Sheets API client service
	 * @throws IOException
	 */
	public static Sheets getSheetsService() throws IOException {
		Credential credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

	// public static void main(String[] args) throws IOException {

	public String updateSheet(Map<String, Double> sheetValuesMap) throws IOException {
		// Build a new authorized API client service.
		Sheets service = getSheetsService();

		// Prints the names and majors of students in a sample spreadsheet:
		// https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
		// String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
		// String range = "Class Data!A2:E";
		/*
		 * ValueRange response = service.spreadsheets().values().get(spreadsheetId,
		 * range).execute(); List<List<Object>> values = response.getValues(); if
		 * (values == null || values.size() == 0) {
		 * System.out.println("No data found."); } else {
		 * System.out.println("Name, Major"); for (List row : values) { // Print columns
		 * A and E, which correspond to indices 0 and 4. System.out.printf("%s, %s\n",
		 * row.get(0), row.get(1)); } }
		 */

		String spreadsheetId = "4365hthefeaw4";
		String valueInputOption = "RAW";

		Iterable<String> cells = sheetValuesMap.keySet();
		List<UpdateValuesResponse> updateValueResponses = new ArrayList<>();
		UpdateValuesResponse writeResponse;

		for (String cell : cells) {
			String range = cell;
			Double value = sheetValuesMap.get(cell);
			List<Object> valueList = new ArrayList<>();
			valueList.add(value);

			List<List<Object>> values = new ArrayList<>();
			values.add(valueList);

			ValueRange body = new ValueRange().setValues(values);

			writeResponse = service.spreadsheets().values().update(spreadsheetId, range, body)
					.setValueInputOption(valueInputOption).execute();
			updateValueResponses.add(writeResponse);
		}

		/*
		 * AppendValuesResponse result =
		 * service.spreadsheets().values().append(spreadsheetId, range, body)
		 * .setValueInputOption(valueInputOption) .execute();
		 */
		return updateValueResponses.toString();

	}

	private static final InvokeGoogleSheets instance = new InvokeGoogleSheets();

	private InvokeGoogleSheets() {
	}

	public static InvokeGoogleSheets getInstance() {
		return instance;
	}

}