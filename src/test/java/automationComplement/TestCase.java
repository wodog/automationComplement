package automationComplement;

import java.io.File;

import org.junit.Test;

import autoComplementForVariableName.IOUtil;

public class TestCase {

	@Test
	public void testPathName() {
		Class<?> cla = TestCase.class;
		System.out.println(cla.getName());
		System.out.println(IOUtil.getQualifiedName(new File("F:/work/wodog/automationComplement/src/main/java/testdata/TestData01.java")));
		String str = IOUtil.getAbsoluteName(TestCase.class);
		System.out.println(str);
	}
}
