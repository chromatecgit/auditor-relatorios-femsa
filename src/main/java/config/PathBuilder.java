package config;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.MyFileVisitor;

public class PathBuilder {

	private Map<String, Path> paths = new HashMap<>();
	private Pattern filePattern = Pattern.compile("_\\d+");

	public void buildFilePaths(String glob, Path filePath) {
		MyFileVisitor mfv = new MyFileVisitor(filePath, glob);
		for (Path path : mfv.getPaths()) {
			this.paths.put(this.formatFileName(path), path);
		}
		mfv = null;
	}
	
	public Map<String, Path> getPathMaps() {
		return paths;
	}
	
	private String formatFileName(final Path path) {
		String fullPathName = path.getName(path.getNameCount() - 1).toString();
		Matcher matcher = filePattern.matcher(fullPathName);
		String cleanName = matcher.replaceAll("");
		return cleanName;
	}
	
}
