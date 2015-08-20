package autoComplementForVariableName;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AutomationComplementServiceTest {
	AutomationComplementService automationComplementService;

	@Before
	public void setUp() {
		automationComplementService = new AutomationComplementService();
	}

	public static void main(String[] args) {

		String line = "private final String PERIOD_START_DATE = \" PERIOD_START_DATE \" ;";
		String result = "private final String periodStartDate = \" periodStartDate \" ;";
		System.out.println(result.matches(".*\".*periodStartDate.*\".*"));
		String results[] = result.split("\".*periodStartDate.*\"");
		String includeDouble=result.substring(results[0].length(), result.length()-results[1].length());
		
		for(String s:results){
			System.out.println(s);
		}
		System.out.println(result);
	}

	@Test
	public void testStart() throws Exception {
		AutomationComplementService.start("e:/repo");
	}

	// @Test
	public void testGetAllClasses() throws Exception {

		System.out.println(IOUtil.getSannerPath());
		List<Class<?>> allClasses = automationComplementService.getAllClasses();
		System.out.println(allClasses.size());
	}

	// @Test
	public void testDoChangeVariableNames() throws Exception {
		List<Class<?>> allClasses = automationComplementService.getAllClasses();
		List<OutputData> outputDatas = automationComplementService
				.doChangeVariableNames(allClasses);
		for (OutputData outputData : outputDatas) {
			System.out.println(outputData);
		}
	}

	// @Test
	public void testFileIO() throws ClassNotFoundException {
		File file = new File(
				"F:/Users/zhoucho/dataloader-hpe/automationComplement/src/main/java/com/hp/it/testdata/TestData01.java");
		System.out.println(IOUtil.getOutputDestination("E:", file));
	}

	// @Test
	public void testDoOutputTo() throws Exception {
		List<Class<?>> allClasses = automationComplementService.getAllClasses();
		List<OutputData> outputDatas = automationComplementService
				.doChangeVariableNames(allClasses);
		automationComplementService.doOutputTo("E:/repo", outputDatas);
	}

	// @Test
	public void testFile() throws FileNotFoundException {
		FileOutputStream file = new FileOutputStream("e:/s/s");
		PrintWriter pw = new PrintWriter(file);
	}
}
