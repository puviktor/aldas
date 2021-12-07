package eu.playerunion.Aldas.system;

import eu.playerunion.Aldas.system.workers.BlessingStatus;

public class Blessing {
	
	private String id;
	private String name;
	private String starter;
	private BlessingStatus status = BlessingStatus.INACTIVE;
	
	/**
	 * Áldás beregisztrálása.
	 * 
	 * @param id Az Áldás egyedi azanosítója.
	 * @param name Az Áldás neve.
	 */
	
	public Blessing(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Az Áldás egyedi azonosítójának kikérése.
	 * 
	 * @return Az Áldás egyedi azonosítója.
	 */
	
	public String getID() {
		return this.id;
	}
	
	/**
	 * Az Áldás nevének kikérése.
	 * 
	 * @return Az Áldás neve.
	 */
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * Az Áldást elindítójának nevének kikérése.
	 * 
	 * @return Az Áldás elindíója.
	 */
	
	public String getStarterName() {
		return this.starter;
	}
	
	/**
	 * Az Áldás indítójának átállítása.
	 * 
	 * @param s Az Áldás elindítója.
	 */
	
	public void setStarter(String s) {
		this.starter = s;
	}
	
	/**
	 * Az Áldás jelenlegi státuszának lekérése.
	 * 
	 * @return Az Áldás jelenlegi státusza.
	 */
	
	public BlessingStatus getStatus() {
		return this.status;
	}
	
	/**
	 * Ez a metódus tickel másodpercenként az Áldás futása esetén.
	 * Ide tedd az Áldás során lefuttatni kívánt dolgaidat.
	 */
	
	public void fire() {}
	
	/**
	 * Ez a metódus közvetlenül az Áldás leállítása során fut le.
	 * Amennyiben az Áldásod megköveteli, hogy alaphelyzetbe álljon, ezzel tudod meghívni a dolgaidat.
	 */
	
	public void extinguish() {}
	
	/**
	 * Az Áldás státuszának módosítása.
	 * 
	 * @param status Az Áldás jelenlegi státusza.
	 */
	
	public void setStatus(BlessingStatus status) {
		this.status = status;
	}
	
	/**
	 * Az Áldás alapvető információinak Stringbe történő konvertálása debugoláshoz, vagy amihez szeretnéd.
	 */
	
	public String toString() {
		return "Blessing{id=" + this.id + ", name=" + this.name + ", starter=" + this.starter + "}";
	}
	
}
