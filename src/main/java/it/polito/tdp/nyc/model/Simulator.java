package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.model.Event.EventType;



public class Simulator {

	/**  USA QUESTO SCHEMA FISSO **/ 

	//paramentri di INPUT: 
    private double probabilita; 
    private int durata;
    

    //Stato del sistema:
	private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
	private Map <String,Integer> numShare;  //tiene conto degli share in real time
	
    // se il value = 1 sta condividendo, altrimenti no.
 
	
	//paramentri di OUTPUT: 
	private Map <String,Integer> idMapTotShare;  //tiene conto degli share totali


	//coda eventi
	private PriorityQueue<Event> queue; 
    
	
	
	
	private List <String> listVertici; 

		
	
	
	
	
	//COSTRUTTORE: 
	public Simulator (SimpleWeightedGraph<String, DefaultWeightedEdge> graph, double probabilita, int durata) {
		
		this.graph= graph;
		this.probabilita=probabilita;
		this.durata=durata; 
	
	}

	

	public void initialize() {
		this.queue= new PriorityQueue<Event>();
		
		this.numShare= new HashMap<String,Integer>(); 
		this.idMapTotShare= new HashMap<String,Integer>(); 
		
		for(String c: this.graph.vertexSet()) {
			this.numShare.put(c, 0);
			this.idMapTotShare.put(c, 0);			
		}
		
		
		this.listVertici = new ArrayList<>(this.graph.vertexSet());

		/** CREO EVENTI INIZIALI, inizializzando la CODA degli eventi **/ 
		for(int t=0; t<100; t++) {
			
			if(Math.random()<= probabilita) {
				
				//allora condivido, su NTA SCELTO A CASO: 
				String nta= verticeCasuale(); 
				
				this.queue.add(new Event(EventType.SHARE, t,
						                 nta, this.durata));
				// t: tiene conto dei giorni, max 100
			}
		}
	}
	
	
	
	//SCELGO UN VERTICE CASUALE: 

	private String verticeCasuale() {		 
		 
		 int n = (int) (Math.random() * this.listVertici.size());
		 return listVertici.get(n);
	}
	
	
	
	

	public void run() {
		
      while (!this.queue.isEmpty()) {
			
			Event e= this.queue.poll();
			
			
			//condizione di uscita:
			if(e.getTime()>=100)
				break; 
			
			// le  3 variabili dell'evento:
			int time= e.getTime();
			String NTA= e.getNTA();
			int durata = e.getDurata(); 
			//DURATA è importante perchè mi dice quando sta per scadere  la condivisione su un NTA
			
			
			switch ( e.getType()) {
			
			
			/** 1o CASO:  **/ 
			case SHARE:
				
				 this.numShare.put(NTA, this.numShare.get(NTA)+1);
				 this.idMapTotShare.put(NTA, this.idMapTotShare.get(NTA)+1);

				 
				 /** NOTA BENISSIMO **/ 
				 //creo un nuovo evento di STOP quando scade la durata di "d" giorni.
				 this.queue.add(new Event(EventType.STOP, time+durata, NTA, 0));
				 
				 
				 
				 // RICONDIVISIONE: 
				 if(durata/2 !=0) { // arrotondo
				 String NtaNuovo= trovaNta(NTA); 
				 
				 if(NtaNuovo!= null) {
					 //ricondivido su questo nuovo 
					 this.queue.add(new Event (EventType.SHARE, time+1, NtaNuovo,durata/2 ));
				   }
				 }

			 break;
			 
			 
			/** 2o CASO:  **/  
			case STOP:
				
			//decremento il numero di file condivisi e BASTA
			 this.numShare.put(NTA, this.numShare.get(NTA)-1);
			 break; 
			}
			
	
      }
	
	}


	private String trovaNta(String nTA) {
		int max= -1; 
		String ntaBest=null; 
		
		//vedo tutti gli archi uscenti da questo: 
		for(DefaultWeightedEdge e: this.graph.outgoingEdgesOf(nTA) ) {
			
			String ntaVicino= Graphs.getOppositeVertex(this.graph, e, nTA);
			int peso= (int) this.graph.getEdgeWeight(e); 
			
			
		if(peso>max && this.numShare.get(ntaVicino)==0) {
			// controllo che quel NTA non stia condividendo ora 
			max= peso;
			ntaBest= ntaVicino;
		 }
			
			
		}
		
		return ntaBest;
	}


	public Map<String, Integer> getIdMapTotShare() {
		return idMapTotShare;
	}


	
}

