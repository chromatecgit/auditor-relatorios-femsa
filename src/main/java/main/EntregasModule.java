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
import enums.ProcessStageEnum;
import exceptions.HaltException;
import exceptions.WarningException;
import model.ReportCell;
import model.ReportDocument;
import model.ReportKeyColumns;
import model.ReportKeyColumnsSyncObj;
import model.ReportRow;
import model.ReportTab;
import utils.EntregasMapHelper;
import utils.FileManager;
import utils.MyLogPrinter;

public class EntregasModule {

	private Pattern filePattern = Pattern.compile("_\\d+");
	private String[] fileNames;

	public EntregasModule(final String[] fileNames) {
		this.fileNames = fileNames;
	}

	public void execute() {
		PathBuilder pathBuilder = new PathBuilder();
		List<ReportDocument> documents = new ArrayList<>();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] { ProjectConfiguration.newFilesPath, ProjectConfiguration.oldFilesPath });

		Map<String, List<EntregasMapHelper>> results = this.organizePathMaps(pathBuilder.getPathMaps());

		for (String fileName : results.keySet()) {
			Collections.sort(results.get(fileName));
			if (!fileName.contains("_VERT")) {
				results.get(fileName).stream().forEach(h -> {
					documents.add(FileManager.fetchDocumentBy(h.getOriginalFileName(), h.getPathFromEntry(),
							ProcessStageEnum.FULL));
				});
				try {
					this.applyBusinessRule(documents);
					documents.clear();
				} catch (HaltException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			} else {
				//TODO: Validar os Verticais por CONCAT E SKU
				continue;
			}
		}
	}

	private void applyBusinessRule(final List<ReportDocument> documents) throws HaltException {
		ReportDocument oldDocument = documents.get(0);
		ReportDocument newDocument = documents.get(1);

		//System.out.println("OLD flag:" + oldDocument.isNew() + " e NEW flag:" + newDocument.isNew());

		MyLogPrinter.addToBuiltMessage(oldDocument.getFileName());

		for (ReportTab oldTab : oldDocument.getTabs()) {
			try {
				
				ReportTab equivalentTab = newDocument.findEquivalentTab(oldTab.getName());
				System.out.println("Comparando aba: " + equivalentTab.getName());
				MyLogPrinter.addToBuiltMessage(oldTab.getName());
				
				this.tabComparator(oldTab, equivalentTab);
			
			} catch (WarningException we) {
				System.out.println("Nenhuma aba encontrada para o nome: " + oldTab.getName());
				we.printStackTrace();
			}
		}
		MyLogPrinter.printBuiltMessage("Diff_" + oldDocument.getFileName());
	}

	private void tabComparator(ReportTab oldTab, ReportTab newTab) {
		for (ReportRow oldRow : oldTab.getRows()) {
			try {
				ReportRow row = oldRow.findEquivalentRowByColumnIndex(newTab.getRows(), "A");
//				System.out.println(
//						"Comparando linha do velho [" + oldRow.getIndex() + "], com o novo [" + row.getIndex() + "]");
				
				List<ReportKeyColumnsSyncObj> synchronizedColumnKeys = this.synchronizeColumns(oldTab.getTableColumns(), newTab.getTableColumns());
				
				this.compareRows(oldRow, row, synchronizedColumnKeys);

			} catch (WarningException e) {
				e.printStackTrace();
			}
		}

	}

	private List<ReportKeyColumnsSyncObj> synchronizeColumns(List<ReportKeyColumns> oldTableColumns, List<ReportKeyColumns> newTableColumns) throws WarningException {
		return oldTableColumns.stream().map(ok -> {
			ReportKeyColumns keyFound = newTableColumns.stream().filter(nk -> nk.getValue().equals(ok.getValue())).findFirst().orElse(null);
			if (keyFound != null) {
				return new ReportKeyColumnsSyncObj(ok.getIndex(), keyFound.getIndex());
			} else {
				throw new WarningException("Nao foi encontrado registro [" + ok.getIndex() + "," + ok.getValue() + "] na entrega nova");
			}
		}).collect(Collectors.toList());
	}

	private void compareRows(ReportRow oldRow, ReportRow row, List<ReportKeyColumnsSyncObj> synchronizedColumnKeys) throws WarningException {
		synchronizedColumnKeys.stream().forEach(k -> {
			ReportCell oldCell = oldRow.findCellByColumn(k.getOldKey());
			ReportCell newCell = row.findCellByColumn(k.getNewKey());
			
			if (!oldCell.getValue().equals(newCell.getValue())) {
				MyLogPrinter.addToBuiltMessage("Para coluna:" + k.getOldKey());
				MyLogPrinter.addToBuiltMessage(
						"\tDiferenca em : " + oldCell.getAddress() + " EV e " + newCell.getAddress() + " EN");
				MyLogPrinter.addToBuiltMessage(
						"\t\tCom valores : " + oldCell.getValue() + " EV e " + newCell.getValue() + " EN");
			}
		});
	}

	private Map<String, List<EntregasMapHelper>> organizePathMaps(final Map<String, Path> pathMaps) {

		return pathMaps.entrySet().stream().map(e -> {
			EntregasMapHelper helper = new EntregasMapHelper();
			helper.setPathEntry(e);
			return helper;
		}).collect(Collectors.groupingBy(EntregasMapHelper::getKey));

	}

}
