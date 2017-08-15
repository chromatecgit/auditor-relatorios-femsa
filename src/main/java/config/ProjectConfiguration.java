package config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe base de configuraçao para aplicacoes
 * 
 * @author Cesar Sportore
 */

public class ProjectConfiguration {
	/**
	 * Diretório raiz padrão da aplicação. Daqui ramificam-se os outros.
	 **/
	private static final String root =
			System.getProperty("user.dir")
					.concat(File.separator)
					.concat("src")
					.concat(File.separator)
					.concat("main")
					.concat(File.separator);
	
	/** 
	 * Constante contendo o endereco dos arquivos de log.
	 *	OBS.: É feita uma concatenação a mais em cada Modulo do Auditor para separar os arquivos em pastas
	 */
	public static final String logFolder = root.concat("logs").concat(File.separator);
	
	public static final Path oldFilesPath = Paths.get(
			root.concat("spreadsheets").concat(File.separator).concat("old").concat(File.separator));
	
	public static final Path newFilesPath = Paths.get(
			root.concat("spreadsheets").concat(File.separator).concat("new").concat(File.separator));
	
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
	
}
