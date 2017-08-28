package config;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import enums.FileClassEnum;
import model.PathBuilderMapValue;
import utils.MyFileVisitor;

public class PathBuilder {

	private Map<String, PathBuilderMapValue> paths = new HashMap<>();

	public void buildFilePaths(String glob, Path... filePaths) {
		for (Path filePath : filePaths) {
			MyFileVisitor mfv = new MyFileVisitor(filePath, glob);
			for (Path path : mfv.getPaths()) {
				String fileName = path.getName(path.getNameCount() - 1).toString();
				PathBuilderMapValue value = new PathBuilderMapValue();
				value.setPath(path);
				FileClassEnum e;
				if (fileName.contains("_VERT")) {
					e = FileClassEnum.VERTICAL;
				} else if (fileName.contains("CONSOLIDADA_SOVI")) {
					e = FileClassEnum.CONSOLIDADA;
				} else {
					e = FileClassEnum.HORIZONTAL;
				}
				value.setFileClass(e);
				this.paths.put(fileName, value);
			}
			mfv = null;
		}
	}
	
	public Map<String, PathBuilderMapValue> getPathMaps() {
		return paths;
	}
}
