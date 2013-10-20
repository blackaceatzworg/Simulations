package at.ac.tuwien.motioncollector.storage;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLScriptHelper {
	
	public static List<String> getStatements(File f)
			throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		StringBuilder sb = new StringBuilder();
		int character;
		int lastCharacter = -1;
		List<String> queries = new ArrayList<String>();

		boolean hasCommentStarted = false;
		try {
			while ((character = br.read()) != -1) {
				if ((char) character == '*' && (char) lastCharacter == '/') {
					hasCommentStarted = true;
					sb.deleteCharAt(sb.length() - 1);
					continue;
				} else if ((char) character == '/'
						&& (char) lastCharacter == '*') {
					hasCommentStarted = false;
					continue;
				} else if ((char) character == '\n') {
					continue;
				}

				if (!hasCommentStarted) {

					sb.append((char) character);
					if (character == ';') {
						queries.add(sb.toString());
						sb = new StringBuilder();
					}
				}
				lastCharacter = character;
			}
			br.close();
		} catch (IOException ex) {
			Logger.getLogger("ERROR").log(Level.SEVERE, null, ex);
		}
		return queries;
	}
}
