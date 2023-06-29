package it.polito.tdp.nyc.model;

public class Event implements Comparable<Event> {

	public enum EventType{
		SHARE,
		STOP
	}
	
	private EventType type ;
	private int time; // conto dei giorni ... 
    private String NTA; 	
    private int durata; //durata residua
    
    

	public Event(EventType type, int time, String nTA, int durata) {
		
		this.type= type; 
		this.time = time;
		this.NTA = nTA;
		this.durata = durata;
	}



	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getTime() {
		return time;
	}


	public void setTime(int time) {
		this.time = time;
	}

	public String getNTA() {
		return NTA;
	}

	public void setNTA(String nTA) {
		NTA = nTA;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}



	@Override
	public int compareTo(Event o) {
		return this.time- o.time;
	}
	
	
	
	
	
	
}
