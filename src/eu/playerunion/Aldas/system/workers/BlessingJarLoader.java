package eu.playerunion.Aldas.system.workers;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import eu.playerunion.Aldas.Main;
import eu.playerunion.Aldas.system.Blessing;

public class BlessingJarLoader {
	
	private File blessingJarFolder = new File("blessings/");
	
	private ArrayList<File> loadedFiles = new ArrayList<File>();
	private ArrayList<Blessing> loadedBlessings = new ArrayList<Blessing>();
	
	public void loadJarFiles() {
		this.loadedFiles.clear();
		
		if(!this.blessingJarFolder.exists())
			this.blessingJarFolder.mkdirs();
		
		for(File f : this.blessingJarFolder.listFiles())
			if(f.isFile() && f.getName().endsWith(".jar"))
				this.loadedFiles.add(f);
		
		this.loadClasses();
	}
	
	@SuppressWarnings("rawtypes")
	public void loadClasses() {
		for(File f : this.loadedFiles) {
			try {
				Main.getInstance().getLogger().info("Fájl betöltésének kísérlete: " + f.getName());
				
				JarFile blessingJar = new JarFile(f.getPath());
				Enumeration<JarEntry> jarEnum = blessingJar.entries();
				ClassLoader classLoader = this.getClass().getClassLoader();
				
				URL[] urls = {new URL("jar:file:" + f.getPath() + "!/")};
				URLClassLoader loader = URLClassLoader.newInstance(urls, classLoader);
				
				while(jarEnum.hasMoreElements()) {
					JarEntry entry = jarEnum.nextElement();
					
					if(entry.isDirectory() || !entry.getName().endsWith(".class"))
						continue;
					
					String className = entry.getName().substring(0, entry.getName().length() - 6);
					
					className = className.replace('/', '.');
					
					if(className.contains("$"))
						continue;
					
					Class c = loader.loadClass(className);
					
					if(c.getSuperclass() == null)
						continue;
					
					if(!c.getSuperclass().equals(Blessing.class)) 
						continue;
					
					Blessing blessing = (Blessing)c.newInstance();
					
					this.loadedBlessings.add(blessing);
					
					Main.getInstance().getLogger().info("Áldás érzékelve és betöltve: " + f.getName());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public ArrayList<Blessing> getLoadedBlessings() {
		return this.loadedBlessings;
	}

}
