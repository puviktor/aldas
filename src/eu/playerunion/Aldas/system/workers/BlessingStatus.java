package eu.playerunion.Aldas.system.workers;

public enum BlessingStatus {
	
	ACTIVE("§aAktív"),
	INACTIVE("§cInaktív");
	
	public final String type;
	
	private BlessingStatus(String type) {
		this.type = type;
	}

}
