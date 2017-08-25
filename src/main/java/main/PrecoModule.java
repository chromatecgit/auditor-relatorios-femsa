package main;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.PathBuilderMapValue;
import model.ReportDocument;
import utils.FileManager;

public class PrecoModule {

	private String[] fileNames;
	
	public PrecoModule(final String[] fileNames) {
		this.fileNames = fileNames;
	}

	public void execute() {
		PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] { ProjectConfiguration.newFilesPath });
		
		Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();
		
		for (String fileName : pathsMap.keySet()) {
			//TODO: fazer a verificacao de vertical ou horizontal aqui
			//FileManager.fetchDocumentsVerticalAndHorizontalFrom(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL);
		}
		
		
		try {
			this.applyBusinessRule(null, false);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}

	private void applyBusinessRule(final List<ReportDocument> documents, boolean isVertical) throws HaltException {
		
	}
	
	

	
}
