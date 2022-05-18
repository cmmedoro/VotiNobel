package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> esami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
		
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
		// ripristino soluzione migliore
		this.migliore = new HashSet<Esame>();
		this.mediaMigliore = 0.0;
		
		Set<Esame> parziale = new HashSet<Esame>();
		//cerca1(parziale, 0, m);
		cerca2(parziale, 0, m);
		
		
		return migliore;	
	}

	private void cerca2(Set<Esame> parziale, int l, int m) {
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > m) 
			return; 
		if(sommaCrediti == m) {
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				this.mediaMigliore = mediaVoti;
				migliore = new HashSet<Esame>(parziale);
			}
			return;
		}
		if( l == esami.size()) 
			return; 
		//proviamo ad aggiungere esami[l]
		parziale.add(esami.get(l));
		cerca2(parziale, l+1, m);
		//proviamo a "non aggiungere" esami[l]
		parziale.remove(esami.get(l));
		cerca2(parziale, l+1, m);
	}

	/*
	 * COMPLESSITA' N! ---> riducibile nella generazione dei sottoproblemi agendo sull'ordine di esplorazione
	 */
	private void cerca1(Set<Esame> parziale, int l, int m) {
		// controllo i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > m) {
			return; //soluzione non valida
		}
		if(sommaCrediti == m) {
			//soluzione valida: controllo se è la migliore (fino a qui)
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				//soluzione promettente
				this.mediaMigliore = mediaVoti;
				migliore = new HashSet<Esame>(parziale);
			}
			return;
		}
		//sicuramente crediti < m
		if( l == esami.size()) {
			return; //questa non sarà una sol. migliore: non ho più niente da aggiungere ---> FINE
		}
		//generiamo i sottoproblemi
		for( Esame e : esami ) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, l+1, m);
				parziale.remove(e);
				// se parziale fosse una lista dovrei rimuovere l'elemento in ultima posizione: parziale.remove(parziale.size()-1);
			}
		}
		
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
