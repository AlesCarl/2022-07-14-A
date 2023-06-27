package it.polito.tdp.nyc.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;


public class Model {
	
	//SEMPLICE, PESATO e NON ORIENTATO
	private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;  // SEMPLICE, PESATO, NON ORIENTATO

	NYCDao dao =new NYCDao() ;

	private HashMap<Integer, Hotspot> idMapHotspot;

	private List<String> allNACode; // VERTICI 
	//private List<Integer> listPesi; 

	
	
	
	public Model() {
		this.dao= new NYCDao() ; 
		this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.allNACode = new ArrayList <>(); 
		this.idMapHotspot= new HashMap<>(); 
		//this.listPesi= new ArrayList <>(); 
		
	}
	
	 public void loadAllNodes(Hotspot h  ) {
	 		
		
		 dao.getAllHotspotMap(idMapHotspot); //solo per creare la mappa con all HOTSPOT
		 allNACode=  dao.getAllNTAcode(h);   // LISTA VERTICI, sono codice NTA
	 		
	 		
	 		
	 	}
	
	 public void creaGrafo( Hotspot h ) {
		 
		 loadAllNodes(h); 
	 		//System.out.println("size: " +this.allNACode.size());

	    	Graphs.addAllVertices(this.graph, allNACode);
	 		System.out.println("vertici GRAFO: " +this.graph.vertexSet().size());
	 		
	 		for(String s1: allNACode) {
	 			for(String s2: allNACode) {
	 				
	 				if(s1.compareTo(s2)!=0) { 
	 		// se fosse stato orientato, cosi era
	 					
	 				int peso= elementiComuni(s1,s2); 
	 				if(peso>0) {
 						Graphs.addEdgeWithVertices(this.graph, s1, s2, peso);
 						//this.listPesi.add(peso); //abbastanza inutile, usa i GRAFI
	 				}
	 			}
	 				
	 		  }
	 		}
			System.out.println("\nnumero ARCHI: "+ this.graph.edgeSet().size());
 
	 }

	 //LAVORA CON i GRAFI 
	
	 double retMedia=0.0;
	 
	 
	 public List<Arco> analisiArchi () {
		 
		 double media= getMedia();   		 
		 //creo List<Arco> con weight > media
		 List<Arco> result = new ArrayList<>(); 
		 
		 for(DefaultWeightedEdge e :this.graph.edgeSet()) {
			 if(this.graph.getEdgeWeight(e)> media) {
				
				 Arco aa=  new Arco( 
						 this.graph.getEdgeSource(e),
						 this.graph.getEdgeTarget(e),
						 (int)graph.getEdgeWeight(e));
				 
				 result.add(aa);
			 }
		 }
		 
		 Collections.sort(result); //ordine decrescente
		 return result;
		 
	 }

	 public double getMedia() {
		 double media= 0.0; 

		 //calcolo MEDIA WEIGHT
		 for(DefaultWeightedEdge e :this.graph.edgeSet()) {
			 media= media+this.graph.getEdgeWeight(e);
		 }
		 media = media/this.graph.edgeSet().size(); // questa è la media
		 
		 return media; 
	 }
	 
//per stavolta va bene così, MA FOTTITI A LAVORARE CON IL GRAFO, no LIST
	
	/* public double getPesoMedio() {
		 int cont= 0; 
		 int sum = 0; 
		 
		 for(int peso: listPesi) {
			 sum+=peso; 
			 cont++; 
		 }
		 
		 
		 return sum/cont; 
		 
	 } */ 
	 
	 
	private int elementiComuni(String s1, String s2) {
		List<String> t1 = this.dao.getSSIDnta(s1);
		List<String> t2 = this.dao.getSSIDnta(s2);
		
		
		for(String ss: t2) {
			if(!t1.contains(ss)) {
				t1.add(ss);
			}
		}
		
		  return t1.size();
	}
	 
	public int getVertici() {
		return graph.vertexSet().size();

	}
    
	public int getNumEdges() {
	     return graph.edgeSet().size(); 
	     
		}




/***************  COLLEGA SIMULAZIONE E MODEL   **************/ 
public  Map<String, Integer>  simula(double probabilita, int durata ) 
{
	Simulator sim = new Simulator(this.graph, probabilita,durata);
	
	sim.initialize();
	sim.run();
	
	return sim.getIdMapTotShare(); 
	//this.nPassiSimulatore= sim.getnPassi();
	//return sim.getStanziali() ;
}





}





