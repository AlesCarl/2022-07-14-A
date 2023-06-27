package it.polito.tdp.nyc.model;

public class Arco implements Comparable<Arco> {
	
	private String v1;
	private String v2;
	private int peso;
	
	
	public Arco(String v1, String v2, int peso) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.peso = peso;
	}
	
	

	@Override
	public String toString() {
		return "Arco: " +v1 + ", " + v2 + ", " + peso +"\n";
	}



	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getV2() {
		return v2;
	}

	public void setV2(String v2) {
		this.v2 = v2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}


	@Override
	public int compareTo(Arco o) {
		//PESO decrescente: 
		return  (o.peso-peso);
		
		/** occhio ad ordinare i double, cosi non è proprio giusto
		 * se fosse un double ... devi usare un "compareTo"
		 * [  peso.compareTo(o.peso) ] per CRESCENTE
		 * [ -peso.compareTo(o.peso) ] per DECRESCENTE
		 *  **/ 
		
	} 
	
	
	
	
	
	

}
