import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A class for writing lines of text to a file or database
 * @author kymed
 *
 */

public class Writer {
	
	File file; // The filepath of the file that's to be written too
	FileWriter fileWriter; // A filewriter to write characters to a file
	BufferedWriter writer; // A BufferedWriter to write buffers to a file
	PrintWriter printWriter; // A print writer to print lines of text to the file
	
	boolean fileOpened; // whether or not the file was opened
	
	/**
	 *  Create the file writer and attempt to open the file
	 * @param filepath the name of the file and it's location/path
	 */
	public Writer(String filepath) {
		
		file = new File(filepath);
		
		fileOpened = true;
		
		try {
			fileWriter = new FileWriter(file, true);
			writer = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(writer);
		} catch (IOException e) {
			System.out.println("file not found. Writer is unsuccessful");
		}
		
	}
	
	/**
	 * Write a line to the file
	 * @param line the line that's to be written
	 */
	public void writeLine(String line) {
		if (fileOpened) {
			try {
				writer.append(line);
				writer.newLine();
			} catch (IOException e) {
				System.out.println("failed to write line");
			}
		}
	}
	
	/**
	 * Close the file once it's done with
	 */
	public void close() {
		try {
			writer.close();
			printWriter.close();
			fileOpened = false;
		} catch (IOException e) {
			System.out.println("file already closed");
		}
	}
}