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
import model.HVPrecoMap;
import model.ReportCell;
import model.ReportDocument;
import model.ReportKeyColumn;
import model.ReportRow;
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
		
		HVPrecoMap horizontalMap = this.parseHorizontalToHVMap(hDoc.getTabs());
		HVPrecoMap verticalMap = this.parseVerticalToHVMap(vDoc.getTabs());
		//this.compare(verticalMap, horizontalMap);
		
		MyLogPrinter.printBuiltMessage("Diff_PRECOS");
	}
	
	private void compare(Map<String, List<HVPrecoInfos>> verticalMap, List<HVPrecoInfos> horizontalMap) {
		MyLogPrinter.printCollection(horizontalMap, "Horizontal_Map");
		MyLogPrinter.printObject(verticalMap, "Vertical_Map");
	}

	private HVPrecoMap parseHorizontalToHVMap(final List<ReportTab> hTabs) {
		HVPrecoMap precoMap = new HVPrecoMap();
		hTabs.parallelStream().forEach(t -> {
			List<ReportKeyColumn> keys = this.filterHorizontalKeyColumns(t.getTableColumns());
			t.getRows().parallelStream().forEach( r -> {
				precoMap.getEntries().add(r.parseReportRowPrecoInfos(keys, true));
			});
			
		});
		System.out.println("Parsed Horizontal");
		return precoMap;
	}
	
	private HVPrecoMap parseVerticalToHVMap(final List<ReportTab> vTabs) {
		HVPrecoMap precoMap = new HVPrecoMap();
		List<ReportRow> freeRows = new ArrayList<>();
		List<ReportKeyColumn> keys = this.filterVerticalKeyColumns(vTabs.iterator().next().getTableColumns());
		
		vTabs.stream().forEach(t -> {
			freeRows.addAll(t.breakTab());
			t = null;
		});
		
		freeRows.stream().map( r -> {
			r.parseReportRowPrecoInfosVertical(keys, false);
		});
		
		return null;
	}
	
	private List<ReportKeyColumn> filterVerticalKeyColumns(List<ReportKeyColumn> tableColumns) {
		List<ReportKeyColumn> collected = tableColumns.stream().map(c -> {
			if (c.getValue().equals("PRODUTO") || c.getValue().equals("PRECO")) {
				return c;
			}
			return null;
		}).collect(Collectors.toList());
		
		while(collected.remove(null));
		
		return collected;
	
	}
	
	private List<ReportKeyColumn> filterHorizontalKeyColumns(List<ReportKeyColumn> tableColumns) {
		List<ReportKeyColumn> collected = tableColumns.stream().map(c -> {
			if (c.getValue().contains("PRECO")) {
				return c;
			}
			return null;
		}).collect(Collectors.toList());
		
		while(collected.remove(null));
		
		return collected;
	
	}
	

	
}
