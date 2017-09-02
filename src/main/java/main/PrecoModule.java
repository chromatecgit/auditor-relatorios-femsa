package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.FileClassEnum;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportSymmetryResults;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;
import utils.TabUtils;

public class PrecoModule {

	private String[] fileNames;
	
	private String[] filters = {"CATEGORIA", "TAMANHO"};
	
	public PrecoModule(final String[] fileNames) {
		this.fileNames = fileNames;
	}

	public void execute() {
		
		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] { ProjectConfiguration.newFilesPath });
		
		final Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();
		
		ReportTab verticalTab = new ReportTab();
		ReportTab horizontalTab = new ReportTab();
		
		for (String fileName : pathsMap.keySet()) {
			if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.VERTICAL.getCode()) {
				verticalTab = TabUtils.merge(
						FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, filters));
				MyLogPrinter.printObject(verticalTab, "verticalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.HORIZONTAL.getCode()) {
				horizontalTab = FileManager.fetchHorizontalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, true);
				MyLogPrinter.printObject(horizontalTab, "horizontalTab");
			}
		}
		
		try {
			this.applyBusinessRule(verticalTab, horizontalTab);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}

	private void applyBusinessRule(final ReportTab verticalTab, final ReportTab horizontalTab) throws HaltException {
		
		this.checkSymmetry(verticalTab, horizontalTab);
		
		List<ReportCellKey> outKeys = new ArrayList<>();
		horizontalTab.getCells().forEach( (key, vCell) -> {
			ReportCell hCell = verticalTab.getCells().get(key);
			if (!vCell.getValue().equals(hCell.getValue())) {
				MyLogPrinter.addToBuiltMessage("[Horizontal]=" + key + " valores=" + hCell + "/[Vertical]=" + key + " valores=" + vCell);
			}
		});
		MyLogPrinter.printObject(outKeys, "PrecoModule_outkeys");
		MyLogPrinter.printBuiltMessage("PrecoModule_diff");
	}

	private void checkSymmetry(ReportTab verticalTab, ReportTab horizontalTab) {
		//Trocar por keys?
		final List<ReportSymmetryResults> asymmetricValues = new ArrayList<>();
		final Map<ReportCellKey, ReportCell> vCells = verticalTab.getCells();
		final Map<ReportCellKey, ReportCell> hCells = horizontalTab.getCells();
		
		vCells.forEach((key, cell) -> {
			ReportCell hCell = hCells.remove(key);
			if (hCell == null) {
				ReportSymmetryResults vResult = new ReportSymmetryResults(key, cell);
				asymmetricValues.add(vResult);
			}
		});
		
		//asymmetricValues.stream().
	}

//	private ReportTab filterTab(ReportTab tab) {
//		tab.getCells().
//	}
}
