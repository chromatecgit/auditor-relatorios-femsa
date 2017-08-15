package main;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import utils.FilesManager;

public class NumeroLinhasModule {
	
	public NumeroLinhasModule() {
		
	}
	
	public static void execute(String[] fileNames) {
		PathBuilder pathBuilder = new PathBuilder();
		pathBuilder.buildFilePaths(
				GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)), ProjectConfiguration.newFilesPath);
		
		Map<String, Path> pathMaps = pathBuilder.getPathMaps();
		//FilesManager.fetchDocumentsBy(Arrays.asList(fileNames));
	}
	
}
