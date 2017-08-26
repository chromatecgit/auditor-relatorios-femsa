package main;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;

public class PrecoModule {

	private String[] fileNames;
	
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
			if (pathsMap.get(fileName).isVertical()) {
				verticalTab = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL);
			} else {
				horizontalTab = FileManager.fetchHorizontalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, true);
			}
		}
		
		try {
			this.applyBusinessRule(verticalTab, horizontalTab);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}

	private void applyBusinessRule(final ReportTab verticalTab, final ReportTab horizontalTab) throws HaltException {
		// Verificar se as duas possuem o mesmo tamanho antes
		// Verificar se é mais eficaz um parallel aqui ou o forEach direto nas entries
		verticalTab.getCells().forEach( (key, vCell) -> {
			ReportCell hCell = horizontalTab.getCells().get(key);
			if (hCell != null) {
				if (!vCell.getValue().equals(hCell.getValue())) {
					MyLogPrinter.addToBuiltMessage("[Horizontal]=" + key + " valores=" + hCell);
					MyLogPrinter.addToBuiltMessage("[Vertical]=" + key + " valores=" + vCell);
				}
			} else {
				if (!key.getColumnName().contains("TAMANHO") && !key.getColumnName().contains("CATEGORIA")) {
					System.out.println("Nao foi encontrada a chave: " + key);
				}
			}
		});
		
		MyLogPrinter.printBuiltMessage("Diff_Preco_PrecoVert");
	}

	
}
