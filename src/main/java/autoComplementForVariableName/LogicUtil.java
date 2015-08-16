package autoComplementForVariableName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LogicUtil {

	public static boolean isStaticAndFinal(Field field) {
		int modifierId = field.getModifiers();
		String modify = Modifier.toString(modifierId);
		if (modify.contains("static") && modify.contains("final")) {
			return true;
		}
		return false;
	}

	public static boolean isUnderline(String fieldName) {
		return fieldName.contains("_");
	}

	public static boolean isInitialUpperCase(Field field) {
		return field.getName().matches("^[A-Z].*$");
	}

	public static String changeVariableNameWithUnderline(Field field) {
		String fieldName = field.getName();
		while (isUnderline(fieldName)) {
			int toUpperCaseIndex = fieldName.indexOf("_") + 1;
			char nextUnderlineChar = fieldName.charAt(toUpperCaseIndex);
			String nextUnderlineCharUpperCased = String.valueOf(
					nextUnderlineChar).toUpperCase();
			fieldName = fieldName.replace("_" + nextUnderlineChar,
					nextUnderlineCharUpperCased);
		}
		if (isInitialUpperCase(field)) {
			fieldName = changeVariableNameWithInitialUpperCase(field);
		}
		return fieldName;
	}

	public static String changeVariableNameWithInitialUpperCase(Field field) {
		String fieldName = field.getName();
		fieldName = fieldName.replace(String.valueOf(fieldName.charAt(0)),
				String.valueOf(fieldName.charAt(0)).toLowerCase());
		return fieldName;
	}

	public static boolean isAllUpperCase(Field field) {
		String strBefore = field.getName();
		String strUpperCase = field.getName().toUpperCase();
		if (strBefore.equals(strUpperCase)) {
			return true;
		}
		return false;
	}

	public static String changeVariableNameWithAllUpperCase(Field field) {
		return field.getName().toLowerCase();
	}

}
