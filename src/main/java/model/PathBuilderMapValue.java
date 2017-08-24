package model;

import java.nio.file.Path;

public class PathBuilderMapValue {
	private boolean isVertical;
	private Path path;

	public boolean isVertical() {
		return isVertical;
	}

	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

}
