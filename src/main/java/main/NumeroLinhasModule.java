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
import exceptions.HaltException;
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
		
		try {
			NumeroLinhasModule.applyBusinessRule(documents);
		} catch (HaltException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void applyBusinessRule(final List<ReportDocument> documents) throws HaltException {
		List<NumeroLinhasResult> results = new ArrayList<>();
		for (ReportDocument document : documents) {
			document.getTabs().stream().forEach(t -> {
				NumeroLinhasResult result = new NumeroLinhasResult();
				result.setFileName(document.getFileName());
				result.setRowQnty(t.getDimensions().getRows());
				result.setTabName(t.getName());
				results.add(result);
			});
		}
		
	
		Map<Integer, List<NumeroLinhasResult>> map = results.stream().collect(Collectors.groupingBy(NumeroLinhasResult::getRowQnty));
		
		if (map.keySet().size() > 1) {
			throw new HaltException("O número de linhas está diferente: \n" + map);
		} else {
			System.out.println("NumeroLinhasModule::SUCCESS!");
		}
	}

}
