package gui.kontroler;

import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import gui.GlavnaForma;
import gui.poslanik_table_model.PoslanikTableModel;
import parlament.api_komunikacija.ParlamentAPIKomunikacija;
import parlament.poslanik.Poslanik;

public class Kontroler {

	private static GlavnaForma gf;
	private static final String lokacija = "data/serviceMembers.json";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					gf = new GlavnaForma();
					gf.setLocationRelativeTo(null);
					gf.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void dodajUStatus(String tekst) {
		gf.getTextAreaStatus().append(tekst + System.lineSeparator());
	}

	public static void vratiISacuvajPoslanikeJson() {
		try {
			JsonArray jsonArray = ParlamentAPIKomunikacija.vratiPoslanikeUJsonFormatu();

			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(lokacija)));

			String tekst = new GsonBuilder().setPrettyPrinting().create().toJson(jsonArray);
			out.println(tekst);

			out.close();

			dodajUStatus("Poslanici su uspesno preuzeti sa servisa.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(gf, "Doslo je do greske prilikom ucitavanja poslanika!", "Greska",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void popuniTabelu() {

		try {

			List<Poslanik> poslanici = deserijalizujPoslanike();

			PoslanikTableModel ptm = (PoslanikTableModel) gf.getTable().getModel();
			ptm.isprazniListu();
			ptm.setPoslanici(poslanici);

			dodajUStatus("Tabela je popunjena poslanicima preuzetim iz servisa.");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(gf, "Doslo je do greske prilikom ucitavanja poslanika!", "Greska",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private static List<Poslanik> deserijalizujPoslanike() throws Exception {
		List<Poslanik> poslanici = new LinkedList<>();

		FileReader in = new FileReader(lokacija);

		JsonArray jsonAray = new GsonBuilder().create().fromJson(in, JsonArray.class);

		in.close();

		JsonObject jsonObject = null;
		Poslanik p = null;

		for (int i = 0; i < jsonAray.size(); i++) {
			jsonObject = (JsonObject) jsonAray.get(i);

			p = new Poslanik();
			p.setId(jsonObject.get("id").getAsInt());
			p.setIme(jsonObject.get("name").getAsString());
			p.setPrezime(jsonObject.get("lastName").getAsString());
			if (jsonObject.get("birthDate") != null) {
				try {
					p.setDatumRodjenja((Date) new SimpleDateFormat("dd.MM.yyyy.")
							.parse(jsonObject.get("birthDate").getAsString()));
				} catch (ParseException e) {
				}
			}

			poslanici.add(p);
		}

		return poslanici;
	}

	public static void updateMembers() {
		PoslanikTableModel ptm = (PoslanikTableModel) gf.getTable().getModel();
		
		List<Poslanik> poslanici = ptm.getPoslanici();
		
		JsonArray jsonArray = ParlamentAPIKomunikacija.prebaciIzListeUJsonNiz(poslanici);
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("data/updatedMembers.json")));
			
			out.println(new GsonBuilder().setPrettyPrinting().create().toJson(jsonArray));
			
			out.close();
			
			dodajUStatus("Izmenjeni poslanici su sacuvani.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(gf, "Doslo je do greske prilikom cuvanja poslanika!", "Greska",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
