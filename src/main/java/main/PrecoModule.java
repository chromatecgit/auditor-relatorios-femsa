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
import enums.DocumentOrientationEnum;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.HVPrecoInfos;
import model.ReportDocument;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;

public class PrecoModule {

	private String[] fileNames;

	public PrecoModule(final String[] fileNames) {
		this.fileNames = fileNames;
	}

	public void execute() {
		PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] { ProjectConfiguration.newFilesPath });

		List<ReportDocument> documents = FileManager.fetchDocumentsBy(pathBuilder.getPathMaps(), ProcessStageEnum.FULL);
		
		try {
			this.applyBusinessRule(documents, false);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}

	private void applyBusinessRule(final List<ReportDocument> documents, boolean isVertical) throws HaltException {
		ReportDocument hDoc = null;
		ReportDocument vDoc = null;
		
		for (ReportDocument d : documents) {
			if (d.getOrientation().equals(DocumentOrientationEnum.HORIZONTAL.getOrientation())) {
				hDoc = d;
			} else {
				vDoc = d;
			}
		}
		
		List<HVPrecoInfos> horizontalMap = this.parseHorizontalToHVMap(hDoc.getTabs());
		Map<String, List<HVPrecoInfos>> verticalMap = this.parseVerticalToHVMap(vDoc.getTabs());
		this.compare(verticalMap, horizontalMap);
		
		MyLogPrinter.printBuiltMessage("Diff_PRECOS");
	}
	
	private void compare(Map<String, List<HVPrecoInfos>> verticalMap, List<HVPrecoInfos> horizontalMap) {
		MyLogPrinter.printCollection(horizontalMap, "Horizontal_Map");
		MyLogPrinter.printObject(verticalMap, "Vertical_Map");
		for (String vKey : verticalMap.keySet()) {
			for (HVPrecoInfos hInfos : horizontalMap) {
				for (HVPrecoInfos vInfos : verticalMap.get(vKey)) {
					if (hInfos.getId().equals(vInfos.getId())) {
						if (hInfos.getSku().equals(vInfos.getSku()) && !hInfos.getPreco().equals(vInfos.getPreco())) {
							MyLogPrinter.addToBuiltMessage("Diferenca de preco para o SKU " + hInfos.getSku() + " com Concat " + hInfos.getId());
							MyLogPrinter.addToBuiltMessage("Horizontal:" + hInfos.getPreco() + " | Vertical: " + vInfos.getPreco());
						}
					}
				}
			}
		}
	}

	private List<HVPrecoInfos> parseHorizontalToHVMap(final List<ReportTab> hTabs) {
		List<HVPrecoInfos> precoInfos = new ArrayList<>();
		hTabs.stream().forEach(t -> {
			t.getRows().stream().forEach(r -> {
				precoInfos.addAll(r.parseReportRowPrecoInfos(t.getTableColumns(), false));
			});
		});
		System.out.println("Parsed Horizontal");
		return precoInfos;
	}
	
	private Map<String, List<HVPrecoInfos>> parseVerticalToHVMap(final List<ReportTab> vTabs) {
		List<HVPrecoInfos> precoInfos = new ArrayList<>();
		vTabs.stream().forEach(t -> {
			t.getRows().stream().forEach(r -> {
				precoInfos.addAll(r.parseReportRowPrecoInfosVertical(t.getTableColumns(), false));
			});
		});
		MyLogPrinter.printCollection(precoInfos, "Preco infos");
		Map<String, List<HVPrecoInfos>> map = precoInfos.stream().collect(Collectors.groupingBy(HVPrecoInfos::getId));
		return map;
	}
	
}
