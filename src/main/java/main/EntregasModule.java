package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.FileClassEnum;
import enums.FilesPerModuleEnum;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import exceptions.WarningException;
import interfaces.Module;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportDocument;
import model.ReportRow;
import model.ReportTab;
import utils.EntregasMapHelper;
import utils.FileManager;
import utils.MyLogPrinter;
import utils.ReportDocumentUtils;

public class EntregasModule implements Module {
	
	private final String[] fileNames = FilesPerModuleEnum.ENTREGAS.getExcelFileNames();

	public EntregasModule() {
	}
	
	@Override
	public void execute() {
		
		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] { ProjectConfiguration.newFilesPath, ProjectConfiguration.oldFilesPath });
		
		final Map<String, PathBuilderMapValue> mixedPathMaps = pathBuilder.getPathMaps();
		
		

//		try {
//			this.applyBusinessRule(verticalTab, horizontalTab);
//		} catch (HaltException e) {
//			e.printStackTrace();
//		}
	}

	private void applyBusinessRule(final List<ReportDocument> documents, boolean isVertical) throws HaltException {
//		ReportDocument oldDocument = documents.get(0);
//		ReportDocument newDocument = documents.get(1);
//
//		//System.out.println("OLD flag:" + oldDocument.isNew() + " e NEW flag:" + newDocument.isNew());
//
//		MyLogPrinter.addToBuiltMessage(oldDocument.getFileName());
//
//		for (ReportTab oldTab : oldDocument.getTabs()) {
//			try {
//				
//				ReportTab equivalentTab = newDocument.findEquivalentTab(oldTab.getName());
//				System.out.println("Comparando aba: " + equivalentTab.getName());
//				MyLogPrinter.addToBuiltMessage(oldTab.getName());
//				
//				this.tabComparator(oldTab, equivalentTab, isVertical);
//			
//			} catch (WarningException we) {
//				System.out.println("Nenhuma aba encontrada para o nome: " + oldTab.getName());
//				we.printStackTrace();
//			}
//		}
//		MyLogPrinter.printBuiltMessage("Diff_" + oldDocument.getFileName());
	}

	private void tabComparator(ReportTab oldTab, ReportTab newTab, boolean isVertical) {
//		for (ReportRow oldRow : oldTab.getRows()) {
//			try {
//				ReportRow row = null;
//				if (!isVertical) {
//					String keyColumn = "A";
//					row = oldRow.findEquivalentRowByColumnIndex(newTab.getRows(), keyColumn);
//				} else {
//					String[] keyColumns = new String[] {"A", oldTab.getTableColumns().stream().filter(c -> c.getValue().equalsIgnoreCase("produto")).findFirst().orElse(null).getIndex()};
//					row = oldRow.findEquivalentRowByColumnIndex(newTab.getRows(), keyColumns);
//				}
//				
////				System.out.println(
////						"Comparando linha do velho [" + oldRow.getIndex() + "], com o novo [" + row.getIndex() + "]");
//				
//				List<ReportKeyColumnSyncObj> synchronizedColumnKeys = this.synchronizeColumns(oldTab.getTableColumns(), newTab.getTableColumns());
//				
//				this.compareRows(oldRow, row, synchronizedColumnKeys);
//
//			} catch (WarningException e) {
//				e.printStackTrace();
//			}
//		}

	}

//	private List<ReportKeyColumnSyncObj> synchronizeColumns(List<ReportKeyColumn> oldTableColumns, List<ReportKeyColumn> newTableColumns) throws WarningException {
//		return oldTableColumns.stream().map(ok -> {
//			ReportKeyColumn keyFound = newTableColumns.stream().filter(nk -> nk.getValue().equals(ok.getValue())).findFirst().orElse(null);
//			if (keyFound != null) {
//				return new ReportKeyColumnSyncObj(ok.getIndex(), keyFound.getIndex());
//			} else {
//				throw new WarningException("Nao foi encontrado registro [" + ok.getIndex() + "," + ok.getValue() + "] na entrega nova");
//			}
//		}).collect(Collectors.toList());
//	}

//	private void compareRows(ReportRow oldRow, ReportRow row, List<ReportKeyColumnSyncObj> synchronizedColumnKeys) throws WarningException {
//		synchronizedColumnKeys.stream().forEach(k -> {
//			ReportCell oldCell = oldRow.findCellByColumn(k.getOldKey());
//			ReportCell newCell = row.findCellByColumn(k.getNewKey());
//			
//			if (!oldCell.getValue().equals(newCell.getValue())) {
//				MyLogPrinter.addToBuiltMessage("Para coluna:" + k.getOldKey());
//				MyLogPrinter.addToBuiltMessage(
//						"\tDiferenca em : " + oldCell.getAddress() + " EV e " + newCell.getAddress() + " EN");
//				MyLogPrinter.addToBuiltMessage(
//						"\t\tCom valores : " + oldCell.getValue() + " EV e " + newCell.getValue() + " EN");
//			}
//		});
//	}

//	private Map<String, List<EntregasMapHelper>> organizePathMaps(final Map<String, Path> pathMaps) {

//		return pathMaps.entrySet().stream().map(e -> {
//			EntregasMapHelper helper = new EntregasMapHelper();
//			helper.setPathEntry(e);
//			return helper;
//		}).collect(Collectors.groupingBy(EntregasMapHelper::getKey));

//	}

}
