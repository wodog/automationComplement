package autoComplementForVariableName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {
	private static List<File> fileCollection = new ArrayList<File>();

	public static String getSannerPath() {
		String filePath = IOUtil.class.getResource("").toString();
		return filePath.substring(6, filePath.indexOf("target"));
	}

	public static List<File> searchAllFiles(String scannerPath) throws Exception {
		File directory = new File(scannerPath);
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				searchAllFiles(file.getAbsolutePath().replace("\\", "/"));
			} else {
				fileCollection.add(file);
			}
		}
		return fileCollection;
	}

	public static String getQualifiedName(File file) {
		String absoluteName = file.getAbsolutePath();
		absoluteName = absoluteName.replace("\\", ".");
		int startIndex = absoluteName.indexOf("src.main.java") + 14;
		int endIndex = absoluteName.lastIndexOf(".java");
		return absoluteName.substring(startIndex, endIndex);
	}

	public static String getAbsoluteName(Class<?> cla) {
		return getSannerPath() + "src/main/java/" + cla.getName().replace(".", "/") + ".java";
	}

	public static boolean isStandardJavaFile(File file) {
		return file.getAbsolutePath().matches("^.*java$");
	}

	public static String getOutputDestination(String destinationPath, File file) {

		return destinationPath + "/" + file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("src")).replace("\\", "/");
	}

}
