package autoComplementForVariableName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class AutomationComplementService {
	private final Logger logger = Logger.getLogger(this.getClass());

	public static void start(String outputDirectory) throws Exception {
		AutomationComplementService automationComplementService = new AutomationComplementService();
		List<Class<?>> allClasses = automationComplementService.getAllClasses();
		List<OutputData> outputDatas = automationComplementService
				.doChangeVariableNames(allClasses);
		automationComplementService.doOutputTo(outputDirectory, outputDatas);
	}

	public List<Class<?>> getAllClasses() throws Exception {
		List<Class<?>> allClasses = new ArrayList<Class<?>>();
		String scannerPath = IOUtil.getSannerPath();
		List<File> filesCollection = IOUtil.searchAllFiles(scannerPath);
		logger.debug("The count of all scanner files : "
				+ filesCollection.size());
		for (File file : filesCollection) {
			if (IOUtil.isStandardJavaFile(file)) {
				String packageName = IOUtil.getQualifiedName(file);
				try {
					allClasses.add(Class.forName(packageName, false,
							IOUtil.class.getClassLoader()));
				} catch (Exception e) {
				} catch (NoClassDefFoundError e) {
				}
			}
		}
		logger.debug("The count of all java files in src/main/java : "
				+ allClasses.size());

		return allClasses;
	}

	public List<OutputData> doChangeVariableNames(List<Class<?>> allClasses) {
		List<OutputData> outputDatas = new ArrayList<OutputData>();
		for (Class<?> cla : allClasses) {
			/*
			 * if(!cla.getSimpleName().equals("TestData03")){ continue; }
			 */
			OutputData outputData = new OutputData();
			boolean isModified = false;
			String needChangeVariableName = null;
			try {
				for (Field field : cla.getDeclaredFields()) {
					field.setAccessible(true);
					if (!LogicUtil.isStaticAndFinal(field)) {
						/*
						 * if (LogicUtil.isUnderline(field.getName())) {
						 * needChangeVariableName = LogicUtil
						 * .changeVariableNameWithUnderline(field);
						 * outputData.getNeedChangeData().put(field.getName(),
						 * needChangeVariableName); isModified = true; } else if
						 * (LogicUtil.isAllUpperCase(field)) {
						 * needChangeVariableName = LogicUtil
						 * .changeVariableNameWithAllUpperCase(field);
						 * outputData.getNeedChangeData().put(field.getName(),
						 * needChangeVariableName); isModified = true; } else if
						 * (LogicUtil .isInitialUpperCase(field.getName())) {
						 * needChangeVariableName = LogicUtil
						 * .changeVariableNameWithInitialUpperCase(field
						 * .getName());
						 * outputData.getNeedChangeData().put(field.getName(),
						 * needChangeVariableName); isModified = true; }
						 */
						if (field.getName().matches("^[a-z].*$")) {
							if (!field.getName().contains("_")) {
								continue;
							}
						}
						needChangeVariableName = LogicUtil
								.changeVariableNameWithUnderline(field);
						outputData.getNeedChangeData().put(field.getName(),
								needChangeVariableName);
						isModified = true;
					}
				}
			} catch (NoClassDefFoundError e) {
			}
			if (isModified) {
				outputData.setNeedChangeFile(new File(IOUtil
						.getAbsoluteName(cla)));
				outputDatas.add(outputData);
			}
		}

		return outputDatas;
	}

	public void doOutputTo(String outputPath, List<OutputData> outputDatas)
			throws Exception {
		BufferedReader fileReader = null;
		PrintWriter printWriter = null;
		logger.debug("The count of has been changed file is : "
				+ outputDatas.size());
		int nameCount = 0;
		for (OutputData outputData : outputDatas) {
			nameCount += outputData.getNeedChangeData().size();
		}
		logger.debug("The count of has been changed name is : " + nameCount);
		logger.debug("");
		logger.debug("");
		for (OutputData outputData : outputDatas) {

			Set<String> keys = outputData.getNeedChangeData().keySet();
			fileReader = new BufferedReader(new FileReader(
					outputData.getNeedChangeFile()));
			File outFile = new File(IOUtil.getOutputDestination(outputPath,
					outputData.getNeedChangeFile()));
			outFile.getParentFile().mkdirs();
			printWriter = new PrintWriter(outFile);
			String line = null;
			StringBuilder content = new StringBuilder();
			logger.debug("-------------------------------------------- file separator -----------------------------------");
			logger.debug("Need change file is : "
					+ outputData.getNeedChangeFile().getAbsolutePath());
			logger.debug("Need change data is : "
					+ outputData.getNeedChangeData());
			logger.debug("Detail(above is changed before, below is changed after) : ");
			logger.debug("");
			while ((line = fileReader.readLine()) != null) {
				for (String key : keys) {
					if (line.matches(".*\\b" + key + "\\b.*")) {
						if (line.trim().startsWith("@")) {
							break;
						}
						logger.debug(line);
						String value = outputData.getNeedChangeData().get(key);
						line = line.replaceAll("\\b" + key + "\\b", value);
						line = IfIsSetOrConstant(line, key, value);
						logger.debug(line);
						logger.debug("");
					}
				}
				content = content.append(line + "\r\n");
			}
			printWriter.print(content);
			printWriter.flush();
		}
		try {
			fileReader.close();
			printWriter.close();
		} catch (Exception e) {
			fileReader = null;
			printWriter = null;
		}
	}

	private String IfIsSetOrConstant(String line, String key, String value) {
		String result = line;
		if (result.contains("=") && result.contains(";")) {
			String[] results = result.split("=");
			results[1].replace(";", "");
			if (results[0].trim().equals(results[1].trim())) {
				String blank = line.substring(0,
						line.indexOf(results[0].trim()));
				result = result.replace(results[0], blank + "this."
						+ results[0].trim());
			}
		}
		if (result.contains("." + value) && !result.contains("this." + value)) {
			result = result.replace("." + value, "." + key);
		}
		if (result.matches(".*\".*" + value + ".*\".*")) {
			String results[] = result.split("\".*" + value + ".*\"");
			String includeDouble = result.substring(results[0].length(),
					result.length() - results[1].length());
			String includeDoubleRevert = includeDouble.replaceAll(value, key);
			result = results[0] + includeDoubleRevert + results[1];
		}
		return result;
	}
}
