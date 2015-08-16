package autoComplementForVariableName;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputData {

	public Map<String, String> needChangeData = new HashMap<String, String>();
	public File needChangeFile;

	public File getNeedChangeFile() {
		return needChangeFile;
	}

	public void setNeedChangeFile(File needChangeFile) {
		this.needChangeFile = needChangeFile;
	}

	public Map<String, String> getNeedChangeData() {
		return needChangeData;
	}

	public void setNeedChangeData(Map<String, String> needChangeData) {
		this.needChangeData = needChangeData;
	}

	@Override
	public String toString() {
		return "OutputData [needChangeData=" + needChangeData
				+ ", needChangeFile=" + needChangeFile + "]";
	}

}
