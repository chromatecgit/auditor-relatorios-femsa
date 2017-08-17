package utils;

import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class EntregasMapHelper {
	private Pattern keyPattern = Pattern.compile("_\\d+");
	private Entry<String, Path> pathEntry;

	public String getKey() {
		return FormattingUtils.formatFileName(this.pathEntry.getKey(), keyPattern);
	}

	public Entry<String, Path> getPathEntry() {
		return pathEntry;
	}

	public void setPathEntry(Entry<String, Path> pathEntry) {
		this.pathEntry = pathEntry;
	}

	@Override
	public String toString() {
		return "EntregasMapHelper [pathEntry=" + pathEntry + "]";
	}

}
