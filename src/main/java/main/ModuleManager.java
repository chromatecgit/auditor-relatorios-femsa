package main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import interfaces.Module;

public class ModuleManager {
	
	public static void startModule(final String moduleName) {
		try {
			
			Class<?> module = Class.forName("main."+moduleName+"Module");
			Constructor<?> constructor = module.getConstructor();
			Module instance = (Module) constructor.newInstance();
			instance.execute();
			
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			
		}
		
	}
}
