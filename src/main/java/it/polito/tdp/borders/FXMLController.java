
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML // fx:id="cmbStato"
    private ComboBox<Country> cmbStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	int anno;
    	
    	try {
    		
    		anno = Integer.parseInt(this.txtAnno.getText());
    		
    		if(anno < 1816 || anno > 2016) {
    			this.txtResult.setText("Inserire un valore compreso tra 1816 e 2016 per l'anno.\n");
    			return;
    		}
    		
    	} catch (NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore numerico per l'anno.");
    		return;
    	}
    	
    	this.model.creaGrafo(anno);
    	
    	this.cmbStato.getItems().addAll(this.model.getVertici());
		
		this.txtResult.setText("Grafo creato.\n");
		this.txtResult.appendText("Ci sono " + this.model.getNumVertici() + " vertici.\n");
		this.txtResult.appendText("Ci sono " + this.model.getNumArchi() + " archi.\n\n");
		
		for(Country c : this.model.getStatiGrado().keySet()) {
			this.txtResult.appendText("Lo stato " + c.getStateName() + " ha grado " + 
										this.model.getStatiGrado().get(c) + ".\n");
		}
		
		this.txtResult.appendText("\nNel grafo ci sono " + this.model.getComponentiConnesse() + 
									" componenti connesse.");

    }
    /*
     * c'è la versione con raggiungibili e quella con raggiungibilix
     * la prima è stata inventata da me con solo il metodo connectedSetOf(vertice) della classse 
     * graphiterator e funziona
     * 
     * la seconda è quella con il deep o breadth first iterator e funziona uguale 
     */
    @FXML
    void doCalcolaConnessi(ActionEvent event) {
    	
    	Country stato = this.cmbStato.getValue();
    	
    	if(stato == null) {
    		this.txtResult.appendText("\nSeleziona un valore dal menù a tendina.");
    		return;
    	}
    		
    	Set<Country> raggiungibili = this.model.getRaggiungibili(stato);
    	//List<Country> raggiungibilix = this.model.getDeepOrBreadth(stato);
    	
    	if(!raggiungibili.isEmpty()) {
    		
    		this.txtResult.appendText("\n\nGli stati raggiungibili via terra partendo da " + stato.toString() + 
    				" sono:\n");
    		for(Country c: raggiungibili) {
    			
    			if(c.equals(stato)) {
    				this.txtResult.appendText("\n");
    			} else {
    				this.txtResult.appendText(c.toString() + "\n");
    			}
    		}
    	}
    	
    	/*
    	 * if(!raggiungibilix.isEmpty()) {
    		
    		this.txtResult.appendText("\n\nGli stati raggiungibili via terra partendo da " + stato.toString() + 
    				" sono:\n");
    		for(Country c: raggiungibilix) {
    			
    			if(c.equals(stato)) {
    				this.txtResult.appendText("\n");
    			} else {
    				this.txtResult.appendText(c.toString() + "\n");
    			}
    		}
    	}
    	 */
    	
    	return;

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbStato != null : "fx:id=\"cmbStato\" was not injected: check your FXML file 'Scene.fxml'.";
        
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
