import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *  Used for reading text from a file
 * @author kymed
 *
 */

public class Reader {
	
	File file; // the path of the file that will be read
	FileReader reader; // A filereader to read characters from the file
	BufferedReader bufferedReader; // A bufferedreader to read the file in buffers
	
	boolean fileOpened = false; // whether or not the file was opened
	String lastLine = null; // the previous read line
	
	/**
	 * Attempt to open the specified file.
	 * @param The file that is going to be read
	 */
	public Reader(String filepath) {
		
		file = new File(filepath);
		try {
			reader = new FileReader(file);
			bufferedReader = new BufferedReader(reader);
			fileOpened = true;
		} catch (FileNotFoundException e) {
			System.out.println("Database failed to load");
		}
		
	}
	
	/**
	 * Attempt to read the next line from the file
	 * @return The line of text read
	 */
	public String getNextLine() {
		if (fileOpened) {
			try {
				return bufferedReader.readLine();
			} catch (IOException e) {
				System.out.println("failed to read line");
			}
		}
		return null;
	}
	
	/**
	 * Close the file once it's finished
	 */
	public void close() {
		try {
			bufferedReader.close();
			fileOpened = false;
		} catch (IOException e) {
			System.out.println("failed to close file");
		}
	}

}
