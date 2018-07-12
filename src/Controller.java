import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller is full of static methods for database handling
 * @author kymed
 *
 */

public class Controller {
	
	private static final String accountdb = "users.txt"; // the database of accounts
	private static final String classdb = "classes.txt"; // the database of classes
	
	/**
	 * Get the account information of a user
	 * @param user
	 * @return array of account information
	 */
	public static String[] getAccount(String user) {
		
		Reader reader = new Reader(accountdb);
		
		String line = "";
		line = reader.getNextLine();
		
		while (line != null) {
			String account[] = line.split(",");
			
			if (account[0].equals(user)) {
				reader.close();
				return account;
			}
			line = reader.getNextLine();
			
		}
		
		reader.close();
		return null;
	}
	
	/**
	 * Checks if a username existing
	 * @param user
	 * @return a boolean on whether or not htat is true
	 */
	public static boolean hasName(String user) {
		
		if (getAccount(user) != null) {
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * Get the password of a user
	 * @param user
	 * @return the password (string)
	 */
	public static String getPass(String user) {
		if (hasName(user)) {
			return getAccount(user)[2];
		}
		return null;
	}
	
	/**
	 * To see if a given password is the correct pasword for a user
	 * @param user
	 * @param pass
	 * @return boolean
	 */
	public static boolean correctPass(String user, String pass) {
		
		if (getPass(user) != null) {
			if (getPass(user).equals(pass)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * Creates a new account to the user database
	 * @param user
	 * @param fullname
	 * @param pass
	 * @param isTeacher
	 */
	public static void createUser(String user, String fullname, String pass, boolean isTeacher) {
		
		 String accountType;
		 
		 if (isTeacher) {
			 accountType = "teacher";
		 } else {
			 accountType = "student";
		 }
		 
		 String dbEntry = user + "," + fullname + "," + pass + "," + accountType;
		 
		 Writer writer = new Writer(accountdb);
		 writer.writeLine(dbEntry);
		 writer.close();
		 
	}
	
	/**
	 * Finds an existing class in the class database
	 * @param classname
	 * @return the Classroom object with no assignments
	 */
	public static Classroom findClass(String classname) {
		// Load Class & Students
			Reader classreader = new Reader(classdb);
			String classline = classreader.getNextLine();
			boolean readingClasses = true;
			boolean classReaderClosed = false;
			boolean classFound = false;
			
			String class_properties[] = null;
			String class_students[] = null;
			
			while (classline != null && readingClasses && !classline.equals("")) {
				class_properties = classline.split("\\?")[0].split(",");
				
				if (classline.split("\\?").length > 1) {
					class_students = classline.split("\\?")[1].split(",");
				} else {
					class_students = null;
				}
				
				if (class_properties[0].equals(classname)) {
					classreader.close();
					readingClasses = false;
					classReaderClosed = true;
					classFound = true;
				}
				
				classline = classreader.getNextLine();
			}
			if (!classFound) {
				classreader.close();
				return null;
			}
			if (!classReaderClosed) {
				classreader.close();
			}
			
			Reader accountReader = new Reader(accountdb);
			String accountLine = "";
			accountLine = accountReader.getNextLine();
			ArrayList<String> studentNames = new ArrayList<String>();
			ArrayList<String> studentFullNames = new ArrayList<String>();
			
			while (accountLine != null) {
				String accountData[] = accountLine.split(",");
				if (class_students != null) {
					for (String classStudent : class_students) {
						if (accountData[0].equals(classStudent)) {
							studentNames.add(classStudent);
							studentFullNames.add(accountData[1]);
						}
					}
				}
				
				accountLine = accountReader.getNextLine();
			}
			accountReader.close();
			
			return new Classroom(class_properties[0], class_properties[1], class_properties[2], studentNames, studentFullNames);
	}
	
	/**
	 * Get all classnames that are affiliated with a certain user
	 * @param user
	 * @return an arraylist of (string) class names
	 */
	public static ArrayList<String> getClassNames(String user) { 
		
		ArrayList<String> classes = new ArrayList<String>();
		
		Reader reader = new Reader(classdb);
		String line = reader.getNextLine();
		
		while (line != null && !line.equals("")) {
			
			if (line.split("\\?")[0].split(",")[2].equals(user)) {
				classes.add(line.split("\\?")[0].split(",")[0]);
			}
			line = reader.getNextLine();
		}
		
		reader.close();
		return classes;
		
	}
	
	/**
	 * Get the classes that are affiliated to a certain student
	 * @param user
	 * @return array list of (String) class names
	 */
	public static ArrayList<String> getStudentClasses(String user) {
		
		ArrayList<String> classes = new ArrayList<String>();
		
		Reader reader = new Reader(classdb);
		String line = reader.getNextLine();
		
		while (line != null && !line.equals("")) {
			if (findClass(line.split("\\?")[0].split(",")[0]).getStudentUsernames().contains(user)) {
				classes.add(line.split("\\?")[0].split(",")[0]);
			}
			
			line = reader.getNextLine();
		}
		
		reader.close();
		return classes;
		
	}
	
	/**
	 * Use (findClass()) to find and create an entire classroom object (with assignments)
	 * based off database information
	 * @param classname
	 * @return the Classroom object
	 */
	public static Classroom getClass(String classname) {
		
		// Load Assignments
		Classroom classRoom = findClass(classname);
		Assignment assignment;
		
		if (classRoom != null) {
			String classpath = "classes/" + classname + ".txt";
			
			File f = new File(classpath);
			if (!f.exists()) {
				return null;
			}
			
			Reader reader = new Reader(classpath);
			String line = "";
			line = reader.getNextLine();
			
			while (line != null && !line.equals("")) {
				String assignmentProperties[] = line.split("\\?")[0].split(",");
				assignment = new Assignment(Integer.parseInt(assignmentProperties[1]), assignmentProperties[0]);
				
				if (line.split("\\?").length > 1) {
					String assignmentStudents[] = line.split("\\?")[1].split(",");

					for (String studentData : assignmentStudents) {
						String studentScore[] = studentData.split("=");
						assignment.addStudent(studentScore[0], Integer.parseInt(studentScore[1]));
					}
					
					for (String student : classRoom.getStudentUsernames()) {
						if (!assignment.getStudents().contains(student)) {
							assignment.addStudent(student, 0);
						}
					}
				}
				
				classRoom.addAssignment(assignment);
				
				line = reader.getNextLine();
			}
			reader.close();
		}
		
		return classRoom;
	}
	
	/**
	 * Delete a class from the database
	 * @param classRoom
	 * @return whether the db action was successful or not
	 */
	public static String delClass(String classRoom)  {
		if (findClass(classRoom) == null) {
			return "class doesn't exist";
		}
		
		List<String> preexistingClasses = new ArrayList<String>();
		Reader reader = new Reader(classdb);
		String line = reader.getNextLine();
		while (line != null && !line.equals("")) {
			
			if(!line.split("\\?")[0].split(",")[0].equals(classRoom)) {
				preexistingClasses.add(line);
			}
			
			line = reader.getNextLine();
			
		}
		reader.close();
		
		File classFile = new File(classdb);
		classFile.delete();
		
		try {
			classFile.createNewFile();
		} catch (IOException e) { return "entire database corrupted. Sorry."; }
		
		Writer writer = new Writer(classdb);
		for (String classline : preexistingClasses) {
			writer.writeLine(classline);
		}
		writer.close();
		//
		String assignmentFilePath = "classes/" + classRoom + ".txt";
		File assignmentFile = new File(assignmentFilePath);
		if(!assignmentFile.delete()) {
			return "class file does not exist";
		}
		
		return "class successfully removed";
	}
	
	/**
	 * Adds a class to the database
	 * @param classRoom
	 * @return whether it was successful or not
	 */
	public static String addClass(Classroom classRoom) {
		
		if (findClass(classRoom.getName()) != null) {
			return "class already exists";
		}
		
		// Write class to official database
		String lineToWrite = classRoom.getCode() + "," + classRoom.getName() + "," + classRoom.getTeacher() + "?";
		for (String student : classRoom.getStudentUsernames()) {
			lineToWrite = lineToWrite + student;
			if ((classRoom.getStudentUsernames().size() - 1) != classRoom.getStudentUsernames().indexOf(student))
			{
				lineToWrite = lineToWrite + ",";
			}
		}
		Writer classwriter = new Writer(classdb);
		classwriter.writeLine(lineToWrite);
		classwriter.close();
		
		// Write create class file and pre-existing class data
		String filepath = "classes/" + classRoom.getCode() + ".txt";
		
		try {
			File file = new File(filepath);
			file.createNewFile();
		} catch (IOException e) { return "file cannot be created"; }
		
		Writer writer = new Writer(filepath);
		for (Assignment a : classRoom.getAssignments()) {
			lineToWrite = a.getName() + "," + a.getWeight() + "?";
			for (String student : a.getStudents()) {
				lineToWrite = lineToWrite + student + "=";
				lineToWrite = lineToWrite + a.getScore(student);
				if ((a.getStudents().size() - 1) != a.getStudents().indexOf(student)) {
					lineToWrite = lineToWrite + ",";
				}
			}
			writer.writeLine(lineToWrite);
		}
		writer.close();
		
		
		return "Class created successfully";
		
	}
	
	/**
	 * Checks if a user is a teacher
	 * @param user
	 * @return boolean
	 */
	public static boolean isTeacher(String user) {
		
		if (getAccount(user)[3].equals("teacher")) {
			return true;
		}
		
		return false;
	}
	
	
}