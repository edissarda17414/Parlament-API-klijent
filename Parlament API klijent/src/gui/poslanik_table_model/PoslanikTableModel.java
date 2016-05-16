package gui.poslanik_table_model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import parlament.poslanik.Poslanik;

public class PoslanikTableModel extends AbstractTableModel {

	private static final String[] columnNames = { "ID", "Name", "Last name", "Birth date" };
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
	private List<Poslanik> poslanici;

	public PoslanikTableModel(List<Poslanik> poslanici) {
		super();

		if (poslanici != null) {
			this.poslanici = poslanici;
		} else {
			this.poslanici = new LinkedList<>();
		}
	}

	public PoslanikTableModel() {
		this.poslanici = new LinkedList<>();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		if (poslanici == null) {
			return 0;
		}

		return poslanici.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Poslanik p = poslanici.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return p.getId();
			case 1:
				return p.getIme();
			case 2:
				return p.getPrezime();
			case 3:
				return (p.getDatumRodjenja() != null) ? sdf.format(p.getDatumRodjenja()) : "nepoznato";
	
			default:
				return "NN";
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return false;
		}

		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Poslanik p = poslanici.get(rowIndex);

		switch (columnIndex) {
			case 1:
				String ime = aValue.toString();
				if (ime != null && !ime.trim().isEmpty()) {
					p.setIme(ime);
				} else {
					
				}
				break;
	
			case 2:
				String prezime = aValue.toString();
				if (prezime != null && !prezime.trim().isEmpty()) {
					p.setPrezime(prezime);
				} else {
					
				}
				break;
	
			case 3:
				String datumString = aValue.toString();
				try {
					Date datum = sdf.parse(datumString);
					p.setDatumRodjenja(datum);
				} catch (ParseException e) {
	
				}
	
				break;
	
			default:
				return;
		}

		fireTableCellUpdated(rowIndex, columnIndex);

	}

	public void setPoslanici(List<Poslanik> poslanici) {
		if (poslanici != null) {
			this.poslanici.clear();
			this.poslanici.addAll(poslanici);
			fireTableDataChanged();
		}
	}

	public void isprazniListu() {
		poslanici = new LinkedList<>();
		fireTableDataChanged();
	}

}
