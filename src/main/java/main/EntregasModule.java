package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.util.SystemOutLogger;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.ReportDocument;
import utils.EntregasMapHelper;
import utils.FileManager;
import utils.FormattingUtils;
import utils.MyLogPrinter;

public class EntregasModule {
	
	private static Pattern filePattern = Pattern.compile("_\\d+");

	public static void execute(String[] fileNames) {
		PathBuilder pathBuilder = new PathBuilder();
		List<ReportDocument> documents = new ArrayList<>();
		
		pathBuilder.buildFilePaths(
				GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] {
						ProjectConfiguration.newFilesPath, 
						ProjectConfiguration.oldFilesPath });
		
		
		EntregasModule.organizePathMaps(pathBuilder.getPathMaps());
		
//		for (String fileName : pathMaps.keySet()) {
//			
//			documents.add(FileManager.fetchDocumentsBy(fileName, pathMaps.get(fileName), ProcessStageEnum.PAGING_100));
//			
//			MyLogPrinter.printCollection(documents, "Documents");
//			try {
//				EntregasModule.applyBusinessRule(documents);
//			} catch (HaltException e) {
//				System.out.println(e.getMessage());
//				e.printStackTrace();
//			}
//		}
	}
	
	private static void organizePathMaps(final Map<String, Path> pathMaps) {
		
		Map<String, List<EntregasMapHelper>> collect2 = pathMaps.entrySet().stream().map(e -> {
			EntregasMapHelper helper = new EntregasMapHelper();
			helper.setPathEntry(e);
			return helper;
		}).collect(Collectors.groupingBy(EntregasMapHelper::getKey));
		
		System.out.println(collect2);
	}

	private static void applyBusinessRule(final List<ReportDocument> documents) throws HaltException {
		
	}
	

}
