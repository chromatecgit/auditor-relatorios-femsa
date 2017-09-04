package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.FileClassEnum;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportSymmetryResult;
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
		
//		this.checkSymmetry(verticalTab, horizontalTab);
	
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

	private void checkSymmetry(final ReportTab verticalTab, final ReportTab horizontalTab) throws HaltException {
		//Trocar por keys?
		final Map<ReportCellKey, ReportCell> asymmetricValues = new TreeMap<>();
		final Map<ReportCellKey, ReportCell> vCells = verticalTab.getCells();
		final Map<ReportCellKey, ReportCell> hCells = horizontalTab.getCells();
		
		System.out.println(vCells.size());
		hCells.forEach((key, cell) -> {
			if (vCells.remove(key) == null) {
				asymmetricValues.put(key, cell);
			}
		});
		
		if (!vCells.isEmpty() || !asymmetricValues.isEmpty()) {
			System.out.println(vCells.size());
			asymmetricValues.putAll(vCells);
			MyLogPrinter.printCollection(this.formatAsymmetricValues(asymmetricValues), "PrecoModule_asymmetricValues");
			throw new HaltException("Existem registros em não conformidade. Favor conferir log do arquivo \"PrecoModule_asymmetricValues\"");
		}
	}

	private Collection<ReportSymmetryResult> formatAsymmetricValues (final Map<ReportCellKey, ReportCell> asymmetricValues) {
		Map<String, ReportSymmetryResult> results = new HashMap<>();
		 asymmetricValues.entrySet().stream().forEach(e -> {
			ReportSymmetryResult r = new ReportSymmetryResult();
			r.setKey(e.getKey().getConcat());
			r.getDescriptions().add(e.getKey().getColumnName() +"="+ e.getValue().getValue() + " ");
			results.merge(e.getKey().getConcat(), r, (nv, ov) -> {
				ov.getDescriptions().addAll(nv.getDescriptions());
				return ov;
			});
		});
		return results.values();
	}
}
