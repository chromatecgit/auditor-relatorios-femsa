package config;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.MyFileVisitor;

public class PathCreator {

	private Map<String, Path> newPathsMap = new HashMap<>();
	private Map<String, Path> oldPathsMap = new HashMap<>();
	private Pattern filePattern = Pattern.compile("_\\d+");

	public void buildFilePaths() {
		
		String glob = "glob:**/*.{xlsx}";

		MyFileVisitor newFileVisitor = new MyFileVisitor(ProjectConfiguration.newFilesPath, glob);
		MyFileVisitor oldFileVisitor = new MyFileVisitor(ProjectConfiguration.oldFilesPath, glob);

		for (Path newPath : newFileVisitor.getPaths()) {
			this.newPathsMap.put(this.formatFileName(newPath), newPath);
		}

		for (Path oldPath : oldFileVisitor.getPaths()) {
			this.oldPathsMap.put(this.formatFileName(oldPath), oldPath);
		}
		
		newFileVisitor = null;
		oldFileVisitor = null;

	}

	public Map<String, Path> getNewPathsMap() {
		return newPathsMap;
	}

	public Map<String, Path> getOldPathsMap() {
		return oldPathsMap;
	}
	
	private String formatFileName(final Path path) {
		String fullPathName = path.getName(path.getNameCount() - 1).toString();
		Matcher matcher = filePattern.matcher(fullPathName);
		String cleanName = matcher.replaceAll("");
		return cleanName;
	}
	
}
