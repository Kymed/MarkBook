import java.util.ArrayList;

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
 * The interface for editing the grades for assignments in a classroom
 * @author kymed
 *
 */

public class GradeManager {

	Stage window; // the window for the application
	Scene back, scene; // the interface to return too and this interface
	Classroom classRoom; // the classroom that will be manipulating
	
	/**
	 * Create the interface and handle the events
	 * @param classRoom
	 * @param back
	 * @param window
	 */
	public GradeManager(Classroom classRoom, Scene back, Stage window) {
		
		// Prepare Interface
		this.back = back;
		this.window = window;
		classRoom.organizeStudentsInAssignments();
		this.classRoom = classRoom;
		
		// Generate Content
		String titleString = classRoom.getCode() + ", Grades";
		Label title = new Label(titleString);
		
		ArrayList<Label> assignments = new ArrayList<Label>();
		ArrayList<Label> students = new ArrayList<Label>();
		ArrayList<String> dbPositions = new ArrayList<String>();
		ArrayList<TextField> scores = new ArrayList<TextField>();
		
		for (String student : classRoom.getStudents()) {
			students.add(new Label(student));
		}
		for (Assignment a : classRoom.getAssignments()) {
			assignments.add(new Label(a.getName()));
			int student = 0;
			for (Integer score : a.getScores()) {
				scores.add(new TextField(Integer.toString(score))); 
				String dbPos = a.getName() + "," + a.getStudents().get(student);
				dbPositions.add(dbPos);
				student += 1;
			}
		}
		
		Button apply_b = new Button("Apply");
		Button back_b = new Button("Back");
		
		// Design Layout
		ScrollPane layout = new ScrollPane();
		VBox content = new VBox();
		
		GridPane grid = new GridPane();
		for (Label s : students) {
			grid.add(s, 0, 1 + students.indexOf(s));
		}
		for (Label a : assignments) {
			grid.add(a, 1 + assignments.indexOf(a), 0);
		}
		int r = 1;
		int c = 1;
		for (TextField s : scores) {
			
			grid.add(s, c, r);
			r += 1;
			if (r == students.size() + 1) {
				r = 1;
				c += 1;
			}
 		}
		
		HBox buttons = new HBox();
		buttons.getChildren().addAll(apply_b, back_b);
		
		content.getChildren().addAll(title, grid, buttons);
		layout.setContent(content);
		
		// Create Interface
		scene = new Scene(layout, 800, 600);
		
		// Handle Events
		back_b.setOnAction(e-> {
			window.setScene(new TeacherClassView(classRoom, back, window).getScene());
		});
		
		apply_b.setOnAction(e->{
			String verification = verifyField(scores);
			if (verification.equals("success")) {
				
				for (int i = 0; i < scores.size(); i++) {
					String assignment = dbPositions.get(i).split(",")[0];
					String student = dbPositions.get(i).split(",")[1];
					classRoom.getAssignment(assignment).changeScore(student, Integer.parseInt(scores.get(i).getText()));
				}
				
				classRoom.applyChanges();
				PopupWindow popup = new PopupWindow("Success", "Changes made successfully");
				window.setScene(new GradeManager(classRoom, back, window).getScene());
			} else {
				PopupWindow popup = new PopupWindow("error", verification);
			}
		});
		
	}
	
	/**
	 * Change scores
	 * @param classRoom
	 * @param scores
	 */
	public void changeScores(Classroom classRoom, ArrayList<TextField> scores) {
		int assignment = 0;
		int student = 0;
		for (TextField score : scores) {
			
		}
	}
	
	/**
	 * Makes sure the fields are numbers and are within an acceptable range
	 * @param fields
	 * @return whether it was successful or not or an error statement
	 */
	public String verifyField(ArrayList<TextField> fields) {
		
		for (TextField field : fields) {
			if (field == null || field.equals("")) {
				return "a field cannot be empty";
			}
			
			if(!field.getText().matches("[0-9]+")) {
				return "Fields can only contain numbers";
			}
			
			int fieldInt = Integer.parseInt(field.getText());
			
			if (!(fieldInt >= 0 && fieldInt <= 100)) {
				return "Scores can only be between 0 and 100";
			}
		}
		
		return "success";
	}
	
	/**
	 * 
	 * @return the interface
	 */
	public Scene getScene() {
		return scene;
	}
	
}
