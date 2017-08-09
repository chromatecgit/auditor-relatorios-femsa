package utils;

import java.util.Optional;

import model.AuditorFile;
import model.AuditorFileGroup;

public class FileRepository {
	
	private static AuditorFileGroup files = new AuditorFileGroup();
	
	public static void addFileToRepository(final AuditorFile file) {
		if (!FileRepository.has(file)) {
			files.getAuditorFiles().add(file);
		}
	}
	
	public static Boolean has(final AuditorFile file) {
		return files.getAuditorFiles().stream().anyMatch(e -> e.equals(file));
	}
	
	public static AuditorFile obtainFileMapWith(final String key) {
		return files.getAuditorFiles().stream().filter(
				e -> key.equals(e.getKey())).findFirst().orElse(processFile(key));
	}
	
	public static AuditorFile processFile(final String key) {
		return null;
	}
}
