package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> grafo;
	private Map<Integer, Country> mappa;
	private BordersDAO dao;

	public Model() {
		
		this.dao = new BordersDAO();
		this.mappa = this.dao.loadAllCountriesMap();
		
	}
	
	/*
	 * allora: prima di fare le cose mi sembra giusto decidere cosa sto facendo e dunque
	 * 
	 * ottengo tutti i confini di mio interesse e li metto nella lista confini tramite il metodo 
	 * getCountryPairs() del dao
	 * 
	 * questa lista contiene implicitamente e duplicati molte volte tutti i vertici del grafo che devo creare
	 * 
	 * e quindi devo scorrere la lista dei confini e ogni volta che uno dei due stati del confine 
	 * non è presente nella lista dei vertici lo devo aggiungere a questa lista
	 */
	
	public void creaGrafo(int anno) {
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		List<Border> confini = this.dao.getCountryPairs(anno, mappa);
		
		List<Country> vertici = new ArrayList<Country>();
		
		for(Border b : confini) {
			
			boolean presente1 = false;
			boolean presente2 = false;
			
			if(vertici.contains(b.getC1()))
				presente1 = true;
			if(vertici.contains(b.getC2()))
				presente2 = true;
			
			if(!presente1)
				vertici.add(b.getC1());
			if(!presente2)
				vertici.add(b.getC2());
		}
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		for(Border b : confini) {
			this.grafo.addEdge(b.getC1(), b.getC2());
		}
		
		System.out.println("Grafo creato.");
		System.out.println("Ci sono: " + this.grafo.vertexSet().size() + " vertici.");
		System.out.println("Ci sono: " + this.grafo.edgeSet().size() + " archi.");
		
	}
	
	public int getNumVertici () {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi () {
		return this.grafo.edgeSet().size();
	}
	
	public Map<Country, Integer> getStatiGrado() {
		
		Map<Country, Integer> mappa = new HashMap<Country, Integer>();
		
		for(Country c : this.grafo.vertexSet()) {
			mappa.put(c, grafo.degreeOf(c));
		}
		
		return mappa;
		
	}

	public int getComponentiConnesse() {
		
		ConnectivityInspector<Country, DefaultEdge> ci = 
				new ConnectivityInspector<Country, DefaultEdge>(this.grafo);
		
		return ci.connectedSets().size();
	}

}





