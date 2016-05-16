package parlament.api_komunikacija;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.html.HTMLParagraphElement;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import parlament.poslanik.Poslanik;

public class ParlamentAPIKomunikacija {

	private static final String membersURL = "http://147.91.128.71:9090/parlament/api/members";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");

	public static List<Poslanik> vratiPoslanike() {
		List<Poslanik> poslanici = new LinkedList<>();

		try {
			String result = sendGet(membersURL);

			JsonArray jsonArray = new GsonBuilder().create().fromJson(result, JsonArray.class);

			Poslanik p = null;
			JsonObject jsonObject = null;

			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = (JsonObject) jsonArray.get(i);

				p = new Poslanik();
				p.setId(jsonObject.get("id").getAsInt());
				p.setIme(jsonObject.get("name").getAsString());
				p.setPrezime(jsonObject.get("lastName").getAsString());

				if (jsonObject.get("birthDate") != null) {
					Date datum = sdf.parse(jsonObject.get("birthDate").getAsString());
					p.setDatumRodjenja(datum);
				}

				poslanici.add(p);
			}

			return poslanici;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return poslanici;
	}

	public static JsonArray vratiPoslanikeUJsonFormatu() throws Exception {
		return new GsonBuilder().setPrettyPrinting().create().fromJson(sendGet(membersURL), JsonArray.class);
	}

	private static String sendGet(String url) throws IOException {
		URL aUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) aUrl.openConnection();
		conn.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String red = null;
		String tekst = "";

		while ((red = in.readLine()) != null) {
			tekst += red;
		}

		return tekst;

	}

}
