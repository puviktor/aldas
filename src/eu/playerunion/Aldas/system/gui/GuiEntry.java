package eu.playerunion.Aldas.system.gui;

import eu.playerunion.Aldas.system.Blessing;

public class GuiEntry {
	
	private int position;
	private Blessing blessing;
	
	public GuiEntry(int position, Blessing blessing) {
		this.position = position;
		this.blessing = blessing;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public Blessing getBlessing() {
		return this.blessing;
	}

}
