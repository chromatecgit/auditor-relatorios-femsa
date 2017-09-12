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
import model.ReportCell;
import model.ReportDocument;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;

public class EntregasModule implements Module {
	
	private final String[] fileNames = FilesPerModuleEnum.ENTREGAS.getExcelFileNames();

	public EntregasModule() {
		
	}
	
	@Override
	public void execute() {
		
		final PathBuilder pathBuilder = new PathBuilder();
		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)), ProjectConfiguration.newFilesPath);
		final Map<String, PathBuilderMapValue> newPathMaps = pathBuilder.getPathMaps();
		
		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)), ProjectConfiguration.oldFilesPath);
		final Map<String, PathBuilderMapValue> oldPathMaps = pathBuilder.getPathMaps();
		
		oldPathMaps.forEach( (ok, ov) -> {
			
			final ReportDocument oldDocument = FileManager.fetchDocument( ok, oldPathMaps.get(ok), ProcessStageEnum.FULL, null );
			final ReportDocument newDocument = FileManager.fetchDocument( ok, newPathMaps.get(ok), ProcessStageEnum.FULL, null );
			
			try {
				this.applyBusinessRule(oldDocument, newDocument);
			} catch (HaltException e) {
				e.printStackTrace();
			}
		});
			
	}

	private void applyBusinessRule(final ReportDocument oldDocument , final ReportDocument newDocument) throws HaltException {
		final List<String> outkeys = new ArrayList<>();
		oldDocument.getTabs().forEach( (ok, ot) -> {
			ReportTab nt = newDocument.getTabs().get(ok);
			ot.getCells().forEach( (ock, oc) -> {
				ReportCell nc = nt.getCells().get(ock);
				if (nc != null) {
					if (!oc.getPocInfos().isEmpty()) {
						
					} else {
						if (!oc.getValue().equals(nc.getValue())) {
							MyLogPrinter.addToBuiltMessage(ock + " em " + ot.getTabName() + " -> [OLD]=" + oc.getValue() + "/[NEW]=" + nc.getValue());
								MyLogPrinter.addToBuiltMessage("\t[OLD_POC]" + oc.getPocInfos());
								MyLogPrinter.addToBuiltMessage("\t[NEW_POC]" + nc.getPocInfos());
						}
					}
				} else {
					outkeys.add(ock + " nao encontrado");
				}
			});
			MyLogPrinter.printBuiltMessage("EntregasModule_diff_" + ok);
			System.out.println("Operacao " + ok + " encerrada");
		});
		
		if (!outkeys.isEmpty()) {
			MyLogPrinter.printCollection(outkeys, "EntregasModule_outkeys");
		}
	}

}
