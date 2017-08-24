package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.poi.util.SystemOutLogger;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.DocumentOrientationEnum;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.HVPrecoEntry;
import model.HVPrecoInfos;
import model.HVPrecoMap;
import model.ReportCell;
import model.ReportDocument;
import model.ReportKeyColumn;
import model.ReportRow;
import model.ReportTab;
import utils.CallCounter;
//CallCounter;
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
		List<ReportTab> hTabs = hDoc.getTabs();
		hDoc = null;
		HVPrecoMap horizontalMap = this.parseHorizontalToHVMap(hTabs);
		List<ReportTab> vTabs = vDoc.getTabs();
		vDoc = null;
		HVPrecoMap verticalMap = this.parseVerticalToHVMap(vTabs);
		//this.compare(verticalMap, horizontalMap);
		//MyLogPrinter.printObject(horizontalMap, "Horizontal_Map");
		MyLogPrinter.printObject(verticalMap, "Vertical_Map");
		
		CallCounter.printResults();
		//MyLogPrinter.printBuiltMessage("Diff_PRECOS");
	}
	
	private void compare(Map<String, List<HVPrecoInfos>> verticalMap, List<HVPrecoInfos> horizontalMap) {
		MyLogPrinter.printCollection(horizontalMap, "Horizontal_Map");
		MyLogPrinter.printObject(verticalMap, "Vertical_Map");
	}

	private HVPrecoMap parseHorizontalToHVMap(final List<ReportTab> hTabs) {
		System.out.println("Executanto [parseHorizontalToHVMap]");
		CallCounter.start();
		HVPrecoMap precoMap = new HVPrecoMap();
		List<ReportKeyColumn> keys = this.filterHorizontalKeyColumns(hTabs.iterator().next().getTableColumns());
		hTabs.parallelStream().forEach(t -> {
			//CallCounter.hTabCounter++;
			t.getRows().parallelStream().forEach( r -> {
				HVPrecoEntry entry = new HVPrecoEntry();
				entry.setId(r.findCellByColumn("A").getValue());
				for (ReportKeyColumn keyColumn : keys) {
					//CallCounter.parseReportRowPrecoInfos_keycolumn++;
					ReportCell cell = r.findCellByColumn(keyColumn.getIndex());
					if (!cell.getValue().equals("0")) {
						HVPrecoInfos infos = new HVPrecoInfos();
						infos.setPreco(cell.getValue());
						infos.setSku(keyColumn.getValue());
						entry.getInfos().add(infos);
					}
				}
				precoMap.getEntries().add(entry);
			});
		});
		CallCounter.stop();
		System.out.println("Parsed Horizontal");
		return precoMap;
	}
	
	private HVPrecoMap parseVerticalToHVMap(final List<ReportTab> vTabs) {
		System.out.println("Executanto [parseVerticalToHVMap]");
		CallCounter.start();
		HVPrecoMap precoMap = new HVPrecoMap();
		List<ReportRow> freeRows = new ArrayList<>();
		List<ReportKeyColumn> keys = this.filterVerticalKeyColumns(vTabs.iterator().next().getTableColumns());
		
		vTabs.stream().forEach(t -> {
			freeRows.addAll(t.breakTab());
			t = null;
		});
		
		Map<String, Long> ids = freeRows.stream().map( fr -> fr.getRowID() ).collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		TreeMap<String, Long> orderedIds = new TreeMap<>(ids);
		//MyLogPrinter.printObject(orderedIds, "Map_Ids");
		
		orderedIds.keySet().parallelStream().forEach( id -> {
			//CallCounter.id_parallel++;
			HVPrecoEntry entry = new HVPrecoEntry();
			entry.setId(id);
			long counter = orderedIds.get(id);
			for (ReportRow row : freeRows) {
				if (counter == 0) {
					break;
				} else if (row.getRowID().equals(id)) {
					counter--;
					HVPrecoInfos infos = new HVPrecoInfos();
					keys.parallelStream().forEach( k -> {
						CallCounter.key_parallel++;
						ReportCell cell = row.findCellByColumn(k.getIndex());
						CallCounter.register();
						if (k.getValue().equals("PRECO")) {
							infos.setPreco(cell.getValue());
						} else if (k.getValue().equals("PRODUTO")) {
							infos.setSku(cell.getValue());	
						}
					});
					entry.getInfos().add(infos);
				}
			}
			precoMap.getEntries().add(entry);
		});
		CallCounter.stopPartial();
		CallCounter.stop();
		return precoMap;
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
