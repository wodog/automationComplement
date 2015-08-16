package automationComplementeForIfOrElseStatement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Description: AutoCompleteUtil is working out for these statement which has not complement with
 * "if" , "else" , "else if".
 *
 * @author: chong-yang.zhou@hp.com
 * @version: 1.0.0
 */
public class AutoCompleteUtil {

	static Logger logger = Logger.getLogger(AutoCompleteUtil.class);

	/**
	 * allFile is saved all has searched files
	 * 
	 * @since 1.0.0
	 */
	static ArrayList<File> allFile = new ArrayList<File>();

	/**
	 * if flag is true ,file will be written and be saved .if flag is false ,on the contrary with
	 * flag is true
	 * 
	 * @since 1.0.0
	 */
	static boolean flag = false;

	/**
	 * has changed file's number
	 * 
	 * @since 1.0.0
	 */
	private static int count = 0;

	/**
	 * count_change is has changed's count
	 * 
	 * @since 1.0.0
	 */
	private static long count_change = 0;

	/**
	 * this program's entry
	 * 
	 * @param directory_path
	 *            a string path which is waiting for search
	 * @param destination
	 *            a string path which is save for files
	 * @throws Exception
	 * @return void
	 * @throws
	 * @since 1.0.0
	 */

	/**
	 * count_check TODO
	 * 
	 * @since 1.0.0
	 */
	private static int count_check = 0;

	public static void start(String directory_path, String destination) throws Exception {
		List<File> files = searchFile(directory_path);
		for (File file : files) {
			if (file.toString().endsWith(".java")) {
				executing(file, destination);
			}
		}
		logger.debug("All searched " + files.size() + " files");
		logger.debug("All executed " + count + " files");
		logger.debug("All changed " + count_change + " places");
		logger.debug("All Need Check " + count_check + " places");
	}

	/**
	 * this function is search all files in a directory
	 * 
	 * @param directory_path
	 *            this param is a string path of directory
	 * @throws Exception
	 * 
	 * @return List<File> return a list contains all has find files in a directory path
	 * @since 1.0.0
	 */
	private static List<File> searchFile(String directory_path) throws Exception {
		File directory = new File(directory_path);
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				searchFile(file.getAbsolutePath().replace("\\", "/"));
			} else {
				allFile.add(file);
			}
		}
		return allFile;
	}

	/**
	 * start executing
	 * 
	 * @param file
	 *            need to executing's file
	 * @param destination
	 *            a string path which is save for files
	 * @throws IOException
	 * @return void
	 * @throws
	 * @since 1.0.0
	 */
	private static void executing(File file, String destination) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		file.getAbsolutePath();
		File out_file = new File((destination + "/" + file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("src"))).replace("\\", "/"));
		out_file.getParentFile().mkdirs();
		out_file.createNewFile();
		PrintWriter out = new PrintWriter(out_file);
		StringBuilder str = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) { // variable line is alone line
			if (line.trim().matches("^[} ]*if *\\(.*\\)$")) { // match if
				str = process(file, line, in, str, "if");
			} else if (line.trim().matches("^[} ]*else *if *\\(.*\\)$")) { // match else if
				str = process(file, line, in, str, "else");
			} else if (line.trim().matches("^[} ]*else$")) { // match else
				str = process(file, line, in, str, "else");
			} else if (line.trim().matches("^[} ]*if *\\(.*\\).*;$")) { // match if (..) syso;
				logger.error(file.getAbsolutePath() + "\r\n" + line);
				logger.error("		This Line Matched 'if (..) syso;' ,So Need To Checking !!!		");
				count_check++;
				str.append(line + "\r\n");
			} else if (line.trim().matches("^[} ]*else.*;$")) {
				logger.error(file.getAbsolutePath() + "\r\n" + line);
				logger.error("		This Line Matched 'else (..) syso;' ,So Need To Checking !!!		");
				count_check++;
				str.append(line + "\r\n");
			} else if (line.trim().matches("^[} ]*else *if *\\(.*\\).*;$")) {
				logger.error(file.getAbsolutePath() + "\r\n" + line);
				logger.error("		This Line Matched 'else if (..) syso;' ,So Need To Checking !!!		");
				count_check++;
				str.append(line + "\r\n");
			} else {
				str.append(line + "\r\n");
			}
		}
		if (flag == true) {
			out.print(str);
			count++;
			flag = false;
			close(in, out);
		} else {
			close(in, out);
			out_file.delete();
		}
	}

	/**
	 * read ,update ,write ,and more operate to file
	 * 
	 * @param file
	 *            the file is to operate
	 * @param line
	 *            the first line
	 * @param in
	 *            BufferedReader
	 * @param str
	 *            str is saved string and waiting for be written
	 * @param format_str
	 *            format
	 * @throws IOException
	 * @return StringBuilder return modified str what is saved string and waiting for be written
	 * @since 1.0.0
	 */
	private static StringBuilder process(File file, String line, BufferedReader in, StringBuilder str, String format_str) throws IOException {
		String second_line = in.readLine();
		if (second_line.trim().endsWith(";")) {
			logger.debug("Searched file :" + file.getName());
			logger.debug("Searched file's absolute path: " + file.getAbsolutePath());
			logger.debug(file.getAbsolutePath() + " Before:\r\n" + line);
			flag = true;
			count_change++;
			line = format(format_str, line, second_line);
			logger.debug(file.getAbsolutePath() + " After:\r\n" + line);
			logger.debug("----------------------------------I'm a delimiter------------------------------");
		} else {
			line = line + "\r\n" + second_line + "\r\n";
		}
		str.append(line);
		return str;
	}

	/**
	 * format last line
	 * 
	 * @param str
	 *            the format string
	 * @param line
	 *            has read the first line
	 * @param second_line
	 *            has read the second line
	 * @return String the format's string
	 * @throws
	 * @since 1.0.0
	 */
	private static String format(String format_str, String line, String second_line) {
		String blank = line.substring(0, line.indexOf(format_str));
		if (blank.split("}").length > 1) {
			line = line.concat(" {\r\n" + second_line + "\r\n" + blank.split("}")[0] + "}\r\n");
		} else {
			line = line.concat(" {\r\n" + second_line + "\r\n" + blank + "}\r\n");
		}
		return line;
	}

	/**
	 * close input and output stream
	 * 
	 * @param in
	 *            is BufferedReader
	 * @param out
	 *            is PrintWriter
	 * @throws IOException
	 * @return void
	 * @throws
	 * @since 1.0.0
	 */
	private static void close(BufferedReader in, PrintWriter out) throws IOException {
		in.close();
		out.close();
	}

}
