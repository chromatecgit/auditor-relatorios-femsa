package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;

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
			if (pathsMap.get(fileName).isVertical()) {
				verticalTab = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, filters);
				MyLogPrinter.printObject(verticalTab, "verticalTab");
			} else {
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
		// Verificar se as duas possuem o mesmo tamanho antes
		List<ReportCellKey> outKeys = new ArrayList<>();
		verticalTab.getCells().forEach( (key, vCell) -> {
			ReportCell hCell = horizontalTab.getCells().get(key);
			if (hCell != null) {
				if (!vCell.getValue().equals(hCell.getValue())) {
					MyLogPrinter.addToBuiltMessage("[Horizontal]=" + key + " valores=" + hCell + "/[Vertical]=" + key + " valores=" + vCell);
				}
			} else {
				outKeys.add(key);
			}
		});
		MyLogPrinter.printObject(outKeys, "PrecoModule_outkeys");
		MyLogPrinter.printBuiltMessage("PrecoModule_diff");
	}

//	private ReportTab filterTab(ReportTab tab) {
//		tab.getCells().
//	}
}
