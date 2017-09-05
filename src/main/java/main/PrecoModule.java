package main;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.FileClassEnum;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import exceptions.WarningException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.SoviConsolidadaCell;
import model.ReportCellKey;
import model.ReportSymmetryResult;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;
import utils.ReportDocumentUtils;

public class PrecoModule {

	private String[] fileNames;
	private String[] filters = {"CATEGORIA", "TAMANHO"};
	private final Map<ReportCellKey, ReportCell> asymmetricValues = new TreeMap<>();
	
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
				verticalTab = ReportDocumentUtils.merge(
						FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, filters));
				MyLogPrinter.printObject(verticalTab, "PrecoModule_verticalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.HORIZONTAL.getCode()) {
				horizontalTab = FileManager.fetchHorizontalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, true);
				MyLogPrinter.printObject(horizontalTab, "PrecoModule_horizontalTab");
			}
		}
		
		try {
			this.applyBusinessRule(verticalTab, horizontalTab);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}

	private void applyBusinessRule(final ReportTab verticalTab, final ReportTab horizontalTab) throws HaltException {
		try {
			this.checkSymmetry(verticalTab, horizontalTab);
		} catch (WarningException we) {
			we.printStackTrace();
		} finally {
			ReportTab newHorizontalTab = this.makeSymmetrical(horizontalTab, asymmetricValues);
			newHorizontalTab.getCells().forEach( (key, hCell) -> {
				ReportCell vCell = verticalTab.getCells().get(key);
				if (!hCell.getValue().equals(vCell.getValue())) {
					MyLogPrinter.addToBuiltMessage("[Horizontal]=" + key + " valores=" + hCell + "/[Vertical]=" + key + " valores=" + vCell);
				}
			});
			
			MyLogPrinter.printBuiltMessage("PrecoModule_diff");
		}
	
	}

	private void checkSymmetry(final ReportTab verticalTab, final ReportTab horizontalTab) {
		final Map<ReportCellKey, ReportCell> vCells = new TreeMap<>();
		vCells.putAll(verticalTab.getCells());
		final Map<ReportCellKey, ReportCell> hCells = new TreeMap<>();
		hCells.putAll(horizontalTab.getCells());
		
		hCells.forEach((key, cell) -> {
			if (vCells.remove(key) == null) {
				asymmetricValues.put(key, cell);
			}
		});
		
		if (!vCells.isEmpty() || !asymmetricValues.isEmpty()) {
			asymmetricValues.putAll(vCells);
			MyLogPrinter.printCollection(
					this.formatAsymmetricValues(asymmetricValues), "PrecoModule_asymmetricValues");
			throw new WarningException(
					"Existem registros em não conformidade. Favor conferir log do arquivo \"PrecoModule_asymmetricValues\"");
		}
		
	}
	
	private ReportTab makeSymmetrical(final ReportTab horizontalTab, final Map<ReportCellKey, ReportCell> asymmetricValues) {
		ReportTab newTab = horizontalTab;
		asymmetricValues.forEach( (k,v) -> {
			ReportCell cell = newTab.getCells().remove(k);
			if (cell == null)  {
				newTab.getCells().put(k, v);
			}
		});
		MyLogPrinter.printObject(newTab, "PrecoModule_newTab");
		return newTab;
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
