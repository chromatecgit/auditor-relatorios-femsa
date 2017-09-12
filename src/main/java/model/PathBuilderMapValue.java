package model;

import java.nio.file.Path;

import enums.FileClassEnum;

public class PathBuilderMapValue {
	private FileClassEnum fileClass;
	private String fileName;
	private Path path;

	public FileClassEnum getFileClass() {
		return fileClass;
	}

	public void setFileClass(FileClassEnum fileClass) {
		this.fileClass = fileClass;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
