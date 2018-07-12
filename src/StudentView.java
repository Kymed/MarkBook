import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The interface for the student to see his/her average and their average in their classes
 * @author kymed
 *
 */

public class StudentView {
	
	Stage window; // the window for the application
	Scene back, scene; // the interface to return too and the interface 
	ClassList classList; // the list of the classes the student is in
	
	public StudentView(ClassList classList, Scene back, Stage window) {
		
		// Prepare Interface
		this.window = window;
		this.back = back;
		this.classList = classList;
		
		// Generate Content
		Label title = new Label("Classes & Join");
		
		List<Button> classes = new ArrayList<Button>();
		List<Label> averages = new ArrayList<Label>();
		for (Classroom classRoom : classList.getClasses()) {
			classes.add(new Button(classRoom.getCode()));
			averages.add(new Label(Integer.toString(classRoom.getStudentScore(classList.getUser()))));
		}
		
		Button logout = new Button("Logout");
		
		Label classcode_l = new Label("Classcode: ");
		TextField classcode_f = new TextField("classcode");
		Button join_b = new Button("Join");
		
		// Design Layout
		ScrollPane layout = new ScrollPane();
		VBox content = new VBox();
		
		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		
		for (Button b : classes) {
			grid.add(b, 0, classes.indexOf(b));
		}
		for (Label l : averages) {
			grid.add(l, 1, averages.indexOf(l));
		}
		grid.add(logout, 0, classes.size());
		grid.add(classcode_l, 2, 0);
		grid.add(classcode_f, 3, 0);
		grid.add(join_b, 3, 1);
		
		content.getChildren().addAll(title, grid);
		layout.setContent(content);
		
		// Create Interface
		scene = new Scene(layout, 800, 600);
		
		// Handle Events
		for (Button b : classes) {
			b.setOnAction(e->{
				window.setScene(new StudentClassView(classList.getClasses().get(classes.indexOf(b)), classList.getUser(), getScene(), window).getScene());
			});
		}
		
		logout.setOnAction(e->{
			window.setScene(back);
		});
		
		join_b.setOnAction(e->{
			String attempt = joinClass(classcode_f.getText());
			if (attempt.equals("success")) { 
				Classroom joinedClass = Controller.getClass(classcode_f.getText());
				joinedClass.addStudent(classList.getUser(), classList.getName());
				classList.addClass(joinedClass);
				classList.applyChanges();
				classList.resetInterface();
			} else {
				PopupWindow popup = new PopupWindow("Error", attempt);
			}
		});
		
	}
	
	/**
	 * 
	 * @return the interface
	 */
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Verifies the information to join a class
	 * @param classCode of the class
	 * @return whether joining it was successful or not
	 */
	public String joinClass(String classCode) {
		if (classCode == null || classCode.equals("")) {
			return "Textfields are empty";
		}
		if (Controller.findClass(classCode) == null) {
			return "Class doesn't exist";
		}
		if (Controller.getClass(classCode).getStudentUsernames().contains(classList.getUser())) {
			return "Already in the class";
		}
		return "success";
	}
	
}
