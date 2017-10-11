package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.FilesPerModuleEnum;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import interfaces.Module;
import model.PathBuilderMapValue;
import model.ReportCellKey;
import model.ReportDocument;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;
import utils.ReportDocumentUtils;

public class SoviPrecoModule implements Module {
	
	private final String[] fileNames = FilesPerModuleEnum.SOVI_PRECO.getExcelFileNames();
	private String[] soviFilters = { "CATEGORIA" };
	private String[] precoFilters = { "CATEGORIA", "TAMANHO" };

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
				soviVerticalDocument = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName),
						ProcessStageEnum.FULL, soviFilters);
				MyLogPrinter.printObject(soviVerticalDocument, "SoviPrecoModule_soviTab");
			} else {
				precoVerticalDocument = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName),
						ProcessStageEnum.FULL, precoFilters);
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
		
		List<String> notFoundInSovi = new ArrayList<>();
		List<String> remainInPreco = new ArrayList<>();
		
		mPrecoVertical.getCells().forEach( (pk, pv) -> {
			if (mSoviVertical.getCells().remove(pk) == null) {
				notFoundInSovi.add(mPrecoVertical.getTabName() + ":" + pk);
			}
		});
		
		MyLogPrinter.printBuiltMessage("SoviPrecoModule_diff_notFoundInSovi");
		
		for (ReportCellKey key : mPrecoVertical.getCells().keySet()) {
			remainInPreco.add(key.toString());
		}
		
		MyLogPrinter.printBuiltMessage("SoviPrecoModule_diff_remainInPreco");

	}
}
