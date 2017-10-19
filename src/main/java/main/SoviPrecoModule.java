package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.FilesPerModuleEnum;
import exceptions.HaltException;
import interfaces.Module;
import model.PathBuilderMapValue;
import model.ReportCellKey;
import model.ReportDocument;
import model.ReportTab;
import utils.FileManager;
import utils.FormattingUtils;
import utils.MyLogPrinter;
import utils.ReportDocumentUtils;

public class SoviPrecoModule implements Module {
	
	private final String[] fileNames = FilesPerModuleEnum.SOVI_PRECO.getExcelFileNames();
	private String[] soviFilters = { "CATEGORIA" };
	private String[] precoFilters = { "CATEGORIA", "TAMANHO"};

	@Override
	public void execute() {
		
		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(
				GlobBuilder.buildGlobPatternWith(
				Arrays.asList(fileNames)), ProjectConfiguration.newFilesPath );

		final Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();

		ReportDocument soviVerticalDocument = new ReportDocument();
		ReportDocument precoVerticalDocument = new ReportDocument();

		for (String fileName : pathsMap.keySet()) {
			if (pathsMap.get(fileName).getFileName().contains("SOVI")) {
				soviVerticalDocument = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName), soviFilters);
				MyLogPrinter.printObject(soviVerticalDocument, "SoviPrecoModule_soviTab");
			} else {
				precoVerticalDocument = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName), precoFilters);
				MyLogPrinter.printObject(precoVerticalDocument, "SoviPrecoModule_precoTab");
			}
		}

		try {
			this.applyBusinessRule(soviVerticalDocument, precoVerticalDocument);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}
	
	private void applyBusinessRule(final ReportDocument soviVerticalDocument, final ReportDocument precoVerticalDocument)
			throws HaltException {
		
		ReportTab mSoviVertical = ReportDocumentUtils.merge(soviVerticalDocument);
		ReportTab mPrecoVertical = ReportDocumentUtils.merge(precoVerticalDocument);
		
		List<ReportCellKey> notFoundInSovi = new ArrayList<>();
		List<ReportCellKey> remainInSovi = new ArrayList<>();
		
		//FIXME: Metodo paliativo
		ReportTab newSoviVertical = this.cleanSoviPrecoPrefix(mSoviVertical);
		ReportTab newPrecoVertical = this.cleanSoviPrecoPrefix(mPrecoVertical);
		
		MyLogPrinter.printObject(newSoviVertical, "SoviPrecoModule_newSoviTab");
		MyLogPrinter.printObject(newPrecoVertical, "SoviPrecoModule_newPrecoTab");
		
		newPrecoVertical.getCells().forEach( (pk, pv) -> {
			if (newSoviVertical.getCells().remove(pk) == null) {
				if (!pk.getColumnName().contains("GDM") && 
						!pk.getColumnName().contains("_PERGUNTA") &&
						!pk.getColumnName().equals("SALESCHANNEL") &&
						!pk.getColumnName().equals("DATA") &&
						!pk.getColumnName().equals("MATRICULA")) {
					notFoundInSovi.add(pk);
				}
			}
		});
		
		MyLogPrinter.printObject(notFoundInSovi, "SoviPrecoModule_diff_notFoundInSovi");
		
		Set<String> collect2 = notFoundInSovi.stream().parallel().map(i -> {
			return i.getColumnName();
		}).collect(Collectors.toSet());
		MyLogPrinter.printObject(collect2, "SoviPrecoModule_diff_notFoundInSovi_skuOnly");
		
		for (ReportCellKey key : newSoviVertical.getCells().keySet()) {
			remainInSovi.add(key);
		}
		
		Set<String> collect3 = remainInSovi.stream().parallel().map(i -> {
			return i.getColumnName();
		}).collect(Collectors.toSet());
		
		MyLogPrinter.printCollection(collect3, "SoviPrecoModule_diff_remainInSovi");

	}

	private ReportTab cleanSoviPrecoPrefix(final ReportTab tab) {
		tab.getCells().forEach((k,v) -> {
			k.setColumnName(FormattingUtils.isolateSkuName(k.getColumnName()));
		});
		return tab;
	}
}
