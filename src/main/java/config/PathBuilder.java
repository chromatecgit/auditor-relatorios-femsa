package config;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import enums.FileClassEnum;
import model.PathBuilderMapValue;
import utils.MyFileVisitor;

public class PathBuilder {

	private Map<String, PathBuilderMapValue> paths = new TreeMap<>();

	public void buildFilePaths(String glob, Path... filePaths) {
		for (Path filePath : filePaths) {
			MyFileVisitor mfv = new MyFileVisitor(filePath, glob);
			for (Path path : mfv.getPaths()) {
				String fileName = this.createFileName(path.getName(path.getNameCount() - 1).toString());
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
	
	private String createFileName(final String fileName) {
		String date = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
		String invertedDate = new StringBuilder(date).reverse().toString();
		String newFileName = fileName.replaceAll("\\d+", invertedDate);
		return newFileName;
	}
	
	public Map<String, PathBuilderMapValue> getPathMaps() {
		return paths;
	}
}
