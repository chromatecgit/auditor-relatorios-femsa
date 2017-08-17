package config;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import utils.MyFileVisitor;

public class PathBuilder {

	private Map<String, Path> paths = new HashMap<>();

	public void buildFilePaths(String glob, Path... filePaths) {
		for (Path filePath : filePaths) {
			MyFileVisitor mfv = new MyFileVisitor(filePath, glob);
			for (Path path : mfv.getPaths()) {
				this.paths.put(path.getName(path.getNameCount() - 1).toString(), path);
			}
			mfv = null;
		}
	}
	
	public Map<String, Path> getPathMaps() {
		return paths;
	}
}
