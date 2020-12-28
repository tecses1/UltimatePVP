package io.github.tecses1.UltimatePVP;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class YmlHandler {

	public static <T> T loadClass(T object, String path) {
		try {
			File file = new File(path);
			YamlConfiguration yml = new YamlConfiguration();
			try {
				yml.load(file);
				
				
				
				Class c = object.getClass();
				
				Field[] fields = c.getFields();
				
				for (Field f : fields) {
					try {

						//Bukkit.getLogger().info("[ULTIMATE PVP] Loading key: " + f.getName());
						Object o;
						if (f.get(object) instanceof Collection) {
							o = yml.getList(f.getName());
						}else {
							o = yml.get(f.getName());
						}
						
						if (o == null) {
							continue;
						}

						f.set(object, o);
						
					}catch(IllegalAccessException e) {
						
					}
				}
				
				
			}catch (InvalidConfigurationException e) {
				Bukkit.getLogger().info("[ULTIMATEPVP] Error loading yaml class at: " + path);

			}
		}catch (IOException e){
			
		}
		return object;
	}
	public static void saveClass (Object object, String path) {
		File thisObject = new File(path);
		YamlConfiguration yml = new YamlConfiguration();
		
		//Bukkit.getLogger().info("[UPVP] Generating reflextion...");
		
		Class c = object.getClass();
		
		Field[] fields = c.getFields();
		
		for (Field f : fields) {
			try {
				Object o = f.get(object);
				
				if (o == null) {
					continue;
				}

				yml.set(f.getName(), o);
				
				
			}catch (IllegalAccessException e) {
				//Bukkit.getLogger().info("[UPVP] Failed setting " + f.getName());

			}
		}
		
		try {
			thisObject.createNewFile();
			yml.save(thisObject);
		}catch(IOException e) {
			
		}
	}
	
	public static <T> T getObjectFromMap(T object, Map<String, Object> map){
		Class c = object.getClass();
		
		Field[] fields = c.getFields();
		
		for (Field f : fields) {
			try {
				Object o = f.get(object);
				
				if (o == null) {
					continue;
				}

				f.set(object, map.get(f.getName()));
				
				
			}catch (IllegalAccessException e) {

			}
		}
		return object;
	}
	
	public static Map<String, Object> getSerializationMap(Object obj) {
        Map<String, Object> serialized = new HashMap<>();
        
		Class c = obj.getClass();
		
		Field[] fields = c.getFields();
		
		for (Field f : fields) {
			try {
				Object o = f.get(obj);
				
				if (o == null) {
					continue;
				}
				
				serialized.put(f.getName(), o);
				
				
			}catch (IllegalAccessException e) {

			}
		}

        return serialized;
	}
}
