import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The interface for when a student views their grades in a class
 * @author kymed
 *
 */

public class StudentClassView {
	
	Scene back, scene; // the scene to return too and the current interface
	Stage window; // the window of the application
	
	/**
	 * Create the interface and handle the events
	 * @param classRoom
	 * @param student
	 * @param back
	 * @param window
	 */
	public StudentClassView(Classroom classRoom, String student, Scene back, Stage window) {
		
		// Prepare Interface
		this.back = back;
		this.window = window;
		
		// Generate content
		String title_s;
		if (classRoom.getCourseAverage() != 0) {
			title_s = classRoom.getCode() + ", " + classRoom.getCourseAverage() + "%";
		} else {
			title_s = classRoom.getCode();
		}
		Label title = new Label(title_s);
		
		ArrayList<Label> assignments = new ArrayList<Label>();
		ArrayList<Label> scores = new ArrayList<Label>();
		if (classRoom.getAssignments() != null) {
			for (Assignment a : classRoom.getAssignments()) {
				assignments.add(new Label(a.getName()));
				String scoreEntry = a.getScore(student) + " (worth " + a.getWeight() + "%)";
				scores.add(new Label(scoreEntry));
			}
		}
		
		Button exit_b = new Button("Back");
		
		// Design layout
		ScrollPane layout = new ScrollPane();
		VBox content = new VBox();
		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		
		int ind = 0;
		for (Label a : assignments) {
			grid.add(a, 0, ind);
			ind += 1;
		}
		ind = 0;
		for (Label s : scores) {
			grid.add(s, 1, ind);
			ind += 1;
		}
		content.getChildren().addAll(title, grid, exit_b);
		layout.setContent(content);
		// Create Interface
		scene = new Scene(layout, 800, 600);
		
		// Handle events
		exit_b.setOnAction(e->{
			window.setScene(back);
		});
		
	}
	
	/**
	 * 
	 * @return the interface
	 */
	public Scene getScene() {
		return scene;
	}

}
