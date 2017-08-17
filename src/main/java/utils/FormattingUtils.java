package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormattingUtils {
	
	public static String formatFileName(final String fileName, final Pattern filePattern) {
		Matcher matcher = filePattern.matcher(fileName);
		String cleanName = matcher.replaceAll("");
		return cleanName;
	}
}
