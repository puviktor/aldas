package eu.playerunion.Aldas;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

import org.json.JSONObject;

public class Util {
	
	private Main main = Main.getInstance();
	
	public JSONObject readJSONFile(File file) {
		JSONObject obj = new JSONObject();
		StringBuilder builder = new StringBuilder("");
		
		try {
			Files.lines(file.toPath()).forEach(s -> builder.append(s));
			
			obj = new JSONObject(builder.toString());
		} catch(Exception e) {
			main.getLogger().severe("Nem sikerült a JSON fájl olvasása: " + e.getMessage());
		}
		
		return obj;
	}
	
	public void writeJSONFile(JSONObject obj, File file) throws Exception {
		if(!file.exists())
			file.createNewFile();
		
		try {
			FileWriter fw = new FileWriter(file);
			
			fw.write(obj.toString(4));
			fw.close();
		} catch(Exception e) {
			main.getLogger().severe("Nem sikerült a JSON fájl írása: " + e.getMessage());
		}
	}

}
