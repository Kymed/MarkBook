import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * An interface for a teacher to view a specific class
 * @author kymed
 *
 */

public class TeacherClassView {
	
	Stage window; // the window for the application
	Scene back, scene; // the interface to return too and the interface
	Classroom classRoom; // the classroom that the teacher is viewing
	
	/**
	 * Create and design the interface
	 * @param classRoom
	 * @param back
	 * @param window
	 */
	public TeacherClassView(Classroom classRoom, Scene back, Stage window) {
		
		// Prepare Interface
		this.window = window;
		this.back = back;
		classRoom.organizeStudentsInAssignments();
		this.classRoom = classRoom;
		
		// Generate Content
		Label title = new Label(classRoom.getCode());
		
		List<Label> assignments = new ArrayList<Label>();
		List<Label> students = new ArrayList<Label>();
		List<Label> scores = new ArrayList<Label>();
		List<Button> remove_bs = new ArrayList<Button>();
		
		for (String student : classRoom.getStudents()) {
			students.add(new Label(student));
			remove_bs.add(new Button("remove"));
		}
		
		for (Assignment a : classRoom.getAssignments()) {
			String atext = a.getName() + " " + a.getWeight() + "%";
			assignments.add(new Label(atext));
			for (Integer _score : a.scores()) {
				scores.add(new Label(_score.toString()));
			}
		}
		
		Button assignmentManager = new Button("Assignment Manager");
		Button gradeManager = new Button("Grade Manager");
		Button backButton = new Button("Back");
		
		// Generate Grid
		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		
		for (Label assignment : assignments) {
			grid.add(assignment, 1 + assignments.indexOf(assignment), 0);
		}
		for (Label student : students) {
			grid.add(student, 0, 1 + students.indexOf(student));
		}
		int c = 1;
		int r = 1;
		for (Label score : scores) {
			grid.add(score, c, r);
			r += 1;
			if (r == students.size() + 1) {
				r = 1;
				c += 1;
			}
		}
		for (Button remove_b : remove_bs) {
			grid.add(remove_b, assignments.size() + 2, 1 + remove_bs.indexOf(remove_b));
		}
		
		// Design Layout
		ScrollPane layout = new ScrollPane();
		VBox content = new VBox();
		HBox buttons = new HBox();
		
		buttons.getChildren().addAll(assignmentManager, gradeManager, backButton);
		content.getChildren().addAll(title, grid, buttons);
		layout.setContent(content);
		
		// Create Interface
		scene = new Scene(layout, 800, 600);
		
		// Handle events
		backButton.setOnAction(e -> {
			window.setScene(back);
		});
		for (Button b : remove_bs) {
			b.setOnAction(e ->{
				classRoom.delStudent(classRoom.getStudentUsernames().get(remove_bs.indexOf(b)));
				classRoom.applyChanges();
				window.setScene(new TeacherClassView(classRoom, back, window).getScene());
			});
		}
		assignmentManager.setOnAction(e->{
			window.setScene(new AssignmentManager(classRoom, back, window).getScene());
		});		
		gradeManager.setOnAction(e->{
			window.setScene(new GradeManager(classRoom, getScene() , window).getScene());
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
