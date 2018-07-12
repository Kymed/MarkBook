import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The classlist of classes that the user will be using when they log in
 * @author kymed
 *
 */

public class ClassList {

	String holder, name; // the username of the holder and their full name
	boolean isTeacher; // whether the user is a teacher or not
	ArrayList<Classroom> classes; // array list of their classes
	Stage window; // the window of the application
	Scene back; // the login interface
	
	/**
	 * Creates the new classlist
	 * @param holder the username of the user
	 * @param name the fullname of the user
	 * @param isTeacher whether or not the user is a teacher or not
	 * @param window the window of the application
	 * @param back the scene the user returns too
	 */
	public ClassList(String holder, String name, boolean isTeacher, Stage window, Scene back) {
		
		this.holder = holder;
		this.name = name;
		this.isTeacher = isTeacher;
		classes = makeClasses();
		
		this.window = window;
		this.back = back;
		
		if (isTeacher) {
			window.setScene(new TeacherView(this, window, back).getScene());
		} else {
			window.setScene(new StudentView(this, back, window).getScene());
		}
		
	}
	
	/**
	 * 
	 * @return the array of classes the user will be using
	 */
	public ArrayList<Classroom> getClasses() {
		return classes;
	}
	
	/**
	 * adds a class to the class list
	 * @param classRoom
	 */
	public void addClass(Classroom classRoom) {
		classes.add(classRoom);
	}
	
	/**
	 * Uses the controller to find the classes that the user will be using
	 * @return an array list of the Classroom objects that the user will be using
	 */
	public ArrayList<Classroom> makeClasses() {
		
		ArrayList<Classroom> classes = new ArrayList<Classroom>();
		
		if (isTeacher) {
			for (String classRoom : Controller.getClassNames(this.holder)) {
				Classroom _classRoom = Controller.getClass(classRoom);
				if (_classRoom != null) {
					classes.add(_classRoom);
				}
			}
		} else {
			
			for (String classRoom : Controller.getStudentClasses(this.holder)) {
				Classroom _classRoom = Controller.getClass(classRoom);
				if (_classRoom != null) {
					classes.add(_classRoom);
				}
			}
		}
		
		return classes;
		
	}
	
	/**
	 * Applies the changes of the classrooms to the databases
	 */
	public void applyChanges() {
		for (Classroom classRoom : classes) {
			classRoom.applyChanges();
		}
	}
	
	/**
	 * 
	 * @return the user's username
	 */
	public String getUser() {
		return holder;
	}
	
	/**
	 * 
	 * @return full name of the user
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return whether the user is a teacher or not
	 */
	public Boolean getIsTeacher() {
		return isTeacher;
	}
	
	/**
	 * reset the current interface after changes were made
	 */
	public void resetInterface() {
		if (isTeacher) {
			classes = makeClasses();
			window.setScene(new TeacherView(this, window, back).getScene());
		} else {
			classes = makeClasses();
			window.setScene(new StudentView(this, back, window).getScene());
		}
	}
}
