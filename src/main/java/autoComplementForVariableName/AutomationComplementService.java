package autoComplementForVariableName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AutomationComplementService {

	public static void start(String outputDirectory) throws Exception {
		AutomationComplementService automationComplementService = new AutomationComplementService();
		List<Class<?>> allClasses = automationComplementService.getAllClasses();
		List<OutputData> outputDatas = automationComplementService.doChangeVariableNames(allClasses);
		automationComplementService.doOutputTo(outputDirectory, outputDatas);
	}

	public List<Class<?>> getAllClasses() throws Exception {
		List<Class<?>> allClasses = new ArrayList<Class<?>>();
		String scannerPath = IOUtil.getSannerPath();
		List<File> filesCollection = IOUtil.searchAllFiles(scannerPath);
		for (File file : filesCollection) {
			if (IOUtil.isStandardJavaFile(file)) {
				String packageName = IOUtil.getQualifiedName(file);
				try {
					allClasses.add(Class.forName(packageName, false, IOUtil.class.getClassLoader()));
				} catch (Exception e) {
				} catch (NoClassDefFoundError e) {
				}
			}
		}
		return allClasses;
	}

	public List<OutputData> doChangeVariableNames(List<Class<?>> allClasses) {
		List<OutputData> outputDatas = new ArrayList<OutputData>();
		for (Class<?> cla : allClasses) {
			OutputData outputData = new OutputData();
			boolean isModified = false;
			String needChangeVariableName = null;
			for (Field field : cla.getDeclaredFields()) {
				field.setAccessible(true);
				if (!LogicUtil.isStaticAndFinal(field)) {
					if (LogicUtil.isUnderline(field.getName())) {
						needChangeVariableName = LogicUtil.changeVariableNameWithUnderline(field);
						outputData.getNeedChangeData().put(field.getName(), needChangeVariableName);
						isModified = true;
					} else if (LogicUtil.isAllUpperCase(field)) {
						needChangeVariableName = LogicUtil.changeVariableNameWithAllUpperCase(field);
						outputData.getNeedChangeData().put(field.getName(), needChangeVariableName);
						isModified = true;
					} else if (LogicUtil.isInitialUpperCase(field)) {
						needChangeVariableName = LogicUtil.changeVariableNameWithInitialUpperCase(field);
						outputData.getNeedChangeData().put(field.getName(), needChangeVariableName);
						isModified = true;
					}
				}
			}
			if (isModified) {
				outputData.setNeedChangeFile(new File(IOUtil.getAbsoluteName(cla)));
				outputDatas.add(outputData);
			}
		}

		return outputDatas;
	}

	public void doOutputTo(String outputPath, List<OutputData> outputDatas) throws Exception {
		BufferedReader fileReader = null;
		PrintWriter printWriter = null;
		for (OutputData outputData : outputDatas) {
			Set<String> keys = outputData.getNeedChangeData().keySet();
			fileReader = new BufferedReader(new FileReader(outputData.getNeedChangeFile()));
			File outFile = new File(IOUtil.getOutputDestination(outputPath, outputData.getNeedChangeFile()));
			outFile.getParentFile().mkdirs();
			printWriter = new PrintWriter(outFile);
			String line = null;
			StringBuilder content = new StringBuilder();
			while ((line = fileReader.readLine()) != null) {
				for (String key : keys) {
					if (line.contains(key) && line.matches("^.*;$")) {
						line = line.replace(key, outputData.getNeedChangeData().get(key));
						if (line.contains("=")) {
							line = line.replace(";", "");
							String[] strs = line.split("=");
							if (strs[0].trim().equalsIgnoreCase(strs[1].trim())) {
								String blank = line.substring(0, line.indexOf(strs[0].trim()));
								line = line.replace(strs[0], blank + "this." + strs[0].trim()) + ";";
							} else {
								line = line + ";";
							}
						}
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
}
