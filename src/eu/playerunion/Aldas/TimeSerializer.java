package eu.playerunion.Aldas;

public class TimeSerializer {
	
	public String serialize(int seconds){
		if(seconds > 3600){
			return this.getLongDurationString(seconds);
		}
		
		return this.getDurationString(seconds);
	}
	
	private String getLongDurationString(int seconds) {
		int hours = seconds / 3600;
	    int minutes = (seconds % 3600) / 60;
	    
	    seconds = seconds % 60;
	    
	    
	    return this.twoDigitString(hours) + ":" + this.twoDigitString(minutes) + ":" + this.twoDigitString(seconds);
	}
	
	private String getDurationString(int seconds) {
	    int minutes = (seconds % 3600) / 60;
	    
	    seconds = seconds % 60;
	    
	    return this.twoDigitString(minutes) + ":" + this.twoDigitString(seconds);
	}

	private String twoDigitString(int number) {
	    if (number == 0) {
	        return "00";
	    }
	    
	    if (number / 10 == 0) {
	        return "0" + number;
	    }
	    
	    return String.valueOf(number);
	}

}
