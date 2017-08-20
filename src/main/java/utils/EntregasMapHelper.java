package utils;

import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class EntregasMapHelper implements Comparable<EntregasMapHelper> {
	private Pattern keyPattern = Pattern.compile("_\\d+");
	private Entry<String, Path> pathEntry;

	public String getKey() {
		return FormattingUtils.formatFileName(this.pathEntry.getKey(), keyPattern);
	}
	
	public String getOriginalFileName() {
		return this.pathEntry.getKey();
	}

	public Entry<String, Path> getPathEntry() {
		return pathEntry;
	}

	public void setPathEntry(Entry<String, Path> pathEntry) {
		this.pathEntry = pathEntry;
	}
	
	public Path getPathFromEntry() {
		return this.pathEntry.getValue();
	}

	@Override
	public String toString() {
		return "EntregasMapHelper [pathEntry=" + pathEntry.getValue() + "]";
	}

	@Override
	public int compareTo(EntregasMapHelper o) {
		if (o.getPathFromEntry().toString().contains("new")) {
			return -1;
		} else if (this.getPathFromEntry().toString().contains("new")) {
			return 1;
		} else {
			return 0;
		}
	}

	
}
