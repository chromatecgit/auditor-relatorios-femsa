package utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyFileVisitor extends SimpleFileVisitor<Path> {

	private final PathMatcher matcher;
	private static int fileCounter = 0;
	private final Path pathTarget;
	private List<Path> filesList;

	public MyFileVisitor(final Path pathTarget, final String glob) {
		this.pathTarget = pathTarget;
		this.matcher = FileSystems.getDefault().getPathMatcher(glob);
		this.filesList = new ArrayList<>();
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Objects.requireNonNull(file);
		Objects.requireNonNull(attrs);
		if (matcher.matches(file)) {
			filesList.add(file);
			fileCounter++;
			System.out.println("\t" + file.getFileName() + " adicionado.");
		} else {
			System.out.println("\t" + file.getFileName() + " recusado.");
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		Objects.requireNonNull(dir);
		if (exc != null)
			throw exc;

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		System.out.println("O arquivo " + file.getFileName() + " nao pode ser lido.");
		return FileVisitResult.CONTINUE;
	}

	public List<Path> getPaths() {
		filesList = new ArrayList<Path>();
		try {
			Files.walkFileTree(pathTarget, this);
			System.out.println("\tArquivos encontrados: " + fileCounter);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filesList;
	}
}
