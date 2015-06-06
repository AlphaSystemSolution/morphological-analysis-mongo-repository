/**
 * 
 */
package com.alphasystem.morphologicalanalysis.repository.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

/**
 * @author sali
 * 
 */
public class DataConversion {

	public void convertData() {
		BufferedReader reader = null;
		PrintWriter writer = null;

		List<String> orginalData = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(new File(
					"locations2.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (reader != null) {
			try {
				String line = reader.readLine();
				while (line != null) {
					orginalData.add(line);
					line = reader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}

		try {
			String pathname = "/Users/sali/development/persistence/mongodb/morphological-analysis-mongo-repository/src/main/resources/data.properties";
			writer = new PrintWriter(new BufferedWriter(new FileWriter(
					new File(pathname))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!orginalData.isEmpty() && writer != null) {
			int lastChapterNumber = 1;
			int lastVerseNumber = 1;
			int lastTokenNumber = 1;
			int lastLocationCount = 2;
			String lastToken = "1:1:1";
			for (String location : orginalData) {
				String[] values = location.split(":");
				int chapterNumber = parseInt(values[0]);
				int verseNumber = parseInt(values[1]);
				int tokenNumber = parseInt(values[2]);
				int locationCount = parseInt(values[3]);
				String token = format("%s:%s:%s", chapterNumber, verseNumber,
						tokenNumber);
				String chapterAndVerse = format("%s_%s", lastChapterNumber,
						lastVerseNumber);
				if (!token.equals(lastToken)) {
					writer.println(format("%s_%s=%s", chapterAndVerse,
							lastTokenNumber, lastLocationCount));
				}
				if (verseNumber != lastVerseNumber) {
					writer.println(format("%s=%s", chapterAndVerse,
							lastTokenNumber));
				}
				lastToken = token;
				lastChapterNumber = chapterNumber;
				lastVerseNumber = verseNumber;
				lastTokenNumber = tokenNumber;
				lastLocationCount = locationCount;
			}
			String chapterAndVerse = format("%s_%s", lastChapterNumber,
					lastVerseNumber);
			writer.println(format("%s_%s=%s", chapterAndVerse, lastTokenNumber,
					lastLocationCount));
			writer.println(format("%s=%s", chapterAndVerse, lastTokenNumber));
			writer.close();
		}

	}
}
