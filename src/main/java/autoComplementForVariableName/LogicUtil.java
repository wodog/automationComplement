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

	public static boolean isInitialUpperCase(String fieldName) {
		return fieldName.matches("^[A-Z].*$");
	}

	public static String changeVariableNameWithUnderline(Field field) {
		String fieldName = field.getName();
		/*if (fieldName.startsWith("_")) {
			fieldName = fieldName.substring(1).toLowerCase();
		} else {
			fieldName = field.getName().toLowerCase();
		}*/
		if (fieldName.matches("^_.*$")) {// 首字母是下划线
			fieldName = fieldName.substring(1);
		}
		if (fieldName.matches("^[A-Z].*$")) { // 首字母大写
			if (fieldName.matches("^.[a-z].*$")) {// 第二字母小写
				char toLowerChar = fieldName.charAt(0);
				String toLowerStr = String.valueOf(toLowerChar).toLowerCase();
				fieldName = fieldName.replaceFirst("" + toLowerChar, toLowerStr);
			}else if(fieldName.matches("^.[A-Z].*[a-z].*$")){
				char toLowerChar = fieldName.charAt(0);
				String toLowerStr = String.valueOf(toLowerChar).toLowerCase();
				fieldName = fieldName.replaceFirst("" + toLowerChar, toLowerStr);
			}else{
				fieldName = fieldName.toLowerCase();
			}
		} else {
			if(fieldName.matches("^._.*$")){
				fieldName = fieldName.toLowerCase();
			}
		}
		while (isUnderline(fieldName)) {
			int toUpperCaseIndex = fieldName.indexOf("_") + 1;
			char nextUnderlineChar = fieldName.charAt(toUpperCaseIndex);
			String nextUnderlineCharUpperCased = String.valueOf(nextUnderlineChar).toUpperCase();
			fieldName = fieldName.replaceFirst("_" + nextUnderlineChar, nextUnderlineCharUpperCased);
		}
		return fieldName;
	}

	public static String changeVariableNameWithInitialUpperCase(String fieldName) {
		fieldName = fieldName.replaceFirst(String.valueOf(fieldName.charAt(0)),
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
