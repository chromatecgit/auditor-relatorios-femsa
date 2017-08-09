package config;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe base de configuraçao para aplicacoes
 * 
 * @author Cesar Sportore
 */

public class ProjectConfiguration {
	
	/** 
	 * Constante contendo o endereco dos arquivos que enviar para publicacao/atualizacao.
	 *	OBS.: E preciso alterar para o endereco da sua maquina
	 */
	public static final Path root = Paths.get("D:\\Porto\\chamados_resources\\sustentacao\\PGP 1295084\\laudo\\LOGS_LAUDO\\23_01_2017\\li1507_23_01_2017\\laudosinistro_service.log.4");
	
	
	public static final Path destinationFile = Paths.get("D:\\Porto\\chamados_resources\\sustentacao\\PGP 1295084\\laudo\\LOGS_LAUDO\\23_01_2017\\li1507_23_01_2017\\laudosinistro_service.log.4_extracted.txt");
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
	 * Array com o nome das pastas de endosso permitidos.
	 * OBS.:Com o array vazio, todas as pastas serao incluidas
	 * 
	 * EX.: "cancelamento","generico"
	 */
	public static final String[] endossos = {
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
	
	/**
	 * Array com os nomes de arquivo a serem incluidos na busca.
	 * - Com o array vazio, todos nomes de arquivo serao incluidos
	 * 
	 * EX.: "orcamento","proposta"
	 */
	public static final String[] fileNames = {
	};
	
}
