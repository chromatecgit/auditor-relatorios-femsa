package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ProcessStageEnum;
import model.NumeroLinhasResult;
import model.ReportDocument;
import utils.FileManager;

public class NumeroLinhasModule {

	public static void execute(String[] fileNames) {
		PathBuilder pathBuilder = new PathBuilder();
		List<ReportDocument> documents = new ArrayList<>();
		
		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				ProjectConfiguration.newFilesPath);
		Map<String, Path> pathMaps = pathBuilder.getPathMaps();
		
		for (String fileName : pathMaps.keySet()) {
			documents.add(FileManager.fetchDocumentBy(fileName, pathMaps.get(fileName), ProcessStageEnum.DIMENSIONS));
		}
		
		NumeroLinhasModule.applyBusinessRule(documents);
	}
	
	private static void applyBusinessRule(final List<ReportDocument> documents) {
		for (ReportDocument document : documents) {
			//document.getTabs().stream().map(t -> t.getDimensions().getRows()).reduce((x,y) -> x == y);
		}
	}

}
