package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public Map<Integer, Country> loadAllCountriesMap() {
		
		String sql = "SELECT ccode, StateAbb, StateNme FROM country";
		Map<Integer, Country> mappaStati = new HashMap<Integer, Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			

			while (rs.next()) {
				Country stato = new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				
				mappaStati.put(rs.getInt("ccode"), stato);
				
			}
			
			conn.close();
			return mappaStati;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	//qua mi prendo le coppie di stati col countrycode filtrate per l'anno così poi scorro la lista o mappa 
	//in cui le metto e ottengo i vertici
	//devo vedere poi se per gli archi devo escludere doppioni e permutazioni
	
	//in pratica con questo metodo ottengo tutti i confini, quindi in pratica gli archi mi sa 
	
	//la query elimina le permutazioni del tipo USA-MEX MEX-USA e sceglie solo una delle due
	public List<Border> getCountryPairs(int anno, Map<Integer, Country> mappaStati) {

		String sql = "SELECT c.`state1no`, c.`state2no` "
				+ "FROM contiguity c "
				+ "WHERE c.`year`<= ? "
				+ "AND c.`state1no` < c.`state2no` "
				+ "AND c.`conttype` = 1";
		List<Border> result = new ArrayList<Border>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Border(mappaStati.get(rs.getInt("state1no")),
						mappaStati.get(rs.getInt("state2no"))));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
}
