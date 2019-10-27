package application.create;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/**
 * This class performs the search process 
 * @author Jacinta
 *
 */
public class SearchManager {
	/**
	 * Retrieves results for the Wikipedia search and writes them to the supplied file
	 * @param file	file for results to be written to
	 * @param term	the term to be searched
	 */
	public void searchTerm(File file, String term) {
		ProcessBuilder builder = new ProcessBuilder("wikit", term);
		try {
			// Search and write to file
			Process process = builder.start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			PrintWriter out = new PrintWriter(new FileWriter(file));

			int exitStatus = process.waitFor();

			// If search process executes without problems, reformat file contents so that each sentence 
			// is on its own line
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					out.println(line);
				}

				out.close();

				String[] cmd = {"sed", "-i", "s/[.] /&\\n/g", file.toString()};
				ProcessBuilder editFile = new ProcessBuilder(cmd);
				Process edit = editFile.start();

				BufferedReader stdout2 = new BufferedReader(new InputStreamReader(edit.getInputStream()));
				BufferedReader stderr2 = new BufferedReader(new InputStreamReader(edit.getErrorStream()));

				int exitStatus2 = edit.waitFor();

				if (exitStatus2 == 0) {
					String line2;
					while ((line2 = stdout2.readLine()) != null) {
						System.out.println(line2);
					}
				} else {
					String line2;
					while ((line2 = stderr2.readLine()) != null) {
						System.err.println(line2);
					}
				}

			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
