package config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe base de configuraçao para aplicacoes
 * 
 * @author Cesar Sportore
 */

public class ProjectConfiguration {
	/**
	 * Diretório raiz padrão da aplicação. Daqui ramificam-se os outros.
	 **/
	private static final String ROOT =
			System.getProperty("user.dir").concat(File.separator);
	
	private static final Path ROOT_PATH = Paths.get(ROOT);
	
	private static final Path ROOT_PARENT = Paths.get(ROOT).getParent();
	
	private static final String LOG_FOLDER_NAME = "logs_auditor";
	
	private static final String SPREADSHEETS_ROOT_FOLDER = "spreadsheets";
	
	private static final String OLD_FILES_FOLDER = SPREADSHEETS_ROOT_FOLDER.concat(File.separator).concat("old").concat(File.separator);
	
	private static final String NEW_FILES_FOLDER = SPREADSHEETS_ROOT_FOLDER.concat(File.separator).concat("new").concat(File.separator);
	
	public static String newLogFolder = "";
	
	public static Path oldFilesPath;
	
	public static Path newFilesPath;
	
	public static final Path sourcePath = Paths.get(
			ROOT.concat("spreadsheets").concat(File.separator).concat("source").concat(File.separator));
	
	public static final String glob = "**.{xlsx}";
	
	/**
	 * Array com o nome das pastas de projetos permitidos.
	 * - Com o array vazio, todas as pastas serao incluidas
	 * ATENCAO: Procure nao usar em conjunto com o blacklist
	 * 
	 * EX.: "127_PetshopClinicasVeterinarias","124_Consultorios"
	 */
	public static final String[] whitelistFolders = {
	};
	
	/** 
	 * Array com o nome das pastas de projetos probidos.
	 * - Com o array vazio, nenhuma pasta sera excluida
	 * 	ATENCAO: Procure nao usar em conjunto com o whitelist
	 * 
	 * EX.: "127_PetshopClinicasVeterinarias","124_Consultorios"
	 */
	public static final String[] blacklistFolders = {
	};
	
	/** Array com as extensoes de arquivo a serem incluidas na busca. */
	public static final String[] fileTypes = {
			"xlsx"
	};
	
	public static void prepareEnvironment() {
		
		try {
			// Creates log directories
			// Para a execucao pelo Eclipse, usar a ROOT_PARENT
			Path logsRootFolder = ROOT_PATH.resolve(LOG_FOLDER_NAME);
			if (!Files.exists(logsRootFolder)) {
				Files.createDirectory(logsRootFolder).toString().concat(File.separator);
			}
			Path finalPath = Files.createDirectory(logsRootFolder.resolve(ProjectConfiguration.buildDateTime()));
			ProjectConfiguration.newLogFolder = finalPath.toString().concat(File.separator);
			
			// Locates spreadsheet folders
			ProjectConfiguration.newFilesPath = ROOT_PATH.resolve(ProjectConfiguration.NEW_FILES_FOLDER);
			ProjectConfiguration.oldFilesPath = ROOT_PATH.resolve(ProjectConfiguration.OLD_FILES_FOLDER);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public static String buildDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
		return localDateTime.format(formatter);
	}
	
}
