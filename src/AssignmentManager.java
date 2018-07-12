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
 * An interface for handling, adding and editing assignments
 * @author kymed
 *
 */

public class AssignmentManager {

	Stage window; // The window that the interface is on
	Scene back, scene; // The scene to return too and the scene of the interface
	Classroom classRoom; // the classroom that the interface will be manipulating
	
	/**
	 * Create a new assignment interface
	 * @param classRoom
	 * @param back
	 * @param window
	 */
	public AssignmentManager(Classroom classRoom, Scene back, Stage window) {
		
		// Prepare Interface
		this.classRoom = classRoom;
		this.back = back;
		this.window = window;
		
		// Generate Content
		String titleString = classRoom.getCode() + "," + " Assignments";
		Label title = new Label(titleString);
		
		ArrayList<Label> assignment_l = new ArrayList<Label>();
		ArrayList<TextField> assignment_f = new ArrayList<TextField>();
		ArrayList<Button> assignment_b = new ArrayList<Button>();
		
		for (Assignment a : classRoom.getAssignments()) {
			assignment_l.add(new Label(a.getName()));
			assignment_f.add(new TextField(Integer.toString(a.getWeight())));
			assignment_b.add(new Button("Delete"));
		}
		
		Label newAssign_l = new Label("New Assignment");
		Label newAssign_n = new Label("Name: ");
		Label newAssign_w = new Label("Weight: ");
		
		TextField newAssign_n_f = new TextField("name");
		TextField newAssign_w_f = new TextField("weight");
		Button newAssign_b = new Button("Add");
		
		Button apply = new Button("Apply");
		Button back_b = new Button("Back");
		
		// Design Layout
		ScrollPane layout = new ScrollPane();
		VBox content = new VBox();
		
		GridPane grid = new GridPane();
		for (int i = 0; i < assignment_l.size(); i++) {
			grid.add(assignment_l.get(i), 0, i);
			grid.add(assignment_f.get(i), 1, i);
			grid.add(assignment_b.get(i), 2, i);
		}
		grid.add(newAssign_l, 0, assignment_l.size());
		grid.add(newAssign_n, 0, assignment_l.size() + 1);
		grid.add(newAssign_w, 0, assignment_l.size() + 2);
		grid.add(newAssign_n_f, 1, assignment_l.size() + 1);
		grid.add(newAssign_w_f, 1, assignment_l.size() + 2);
		grid.add(newAssign_b, 2, assignment_l.size() + 2);
		
		HBox buttons = new HBox();
		buttons.getChildren().addAll(apply, back_b);
		
		content.getChildren().addAll(title, grid, buttons);
		layout.setContent(content);
		
		// Create Interface
		scene = new Scene(layout, 800, 600);
		
		// Handle events
		
		apply.setOnAction(e->{
			String verification = verifyWeightings(assignment_f);
			if (verification.equals("success")) {
				for(Assignment a : classRoom.getAssignments()) {
					classRoom.editAssignmentWeighting(a.getName(), Integer.parseInt(assignment_f.get(classRoom.getAssignments().indexOf(a)).getText()));
				}
				classRoom.applyChanges();
				PopupWindow popup = new PopupWindow("success", "Changes were made successfully");
				window.setScene(new AssignmentManager(classRoom, back, window).getScene());
			} else {
				PopupWindow popup = new PopupWindow("error", verification);
			}
		});
		
		back_b.setOnAction(e->{
			window.setScene(new TeacherClassView(classRoom, back, window).getScene());
		});
		
		newAssign_b.setOnAction(e->{
			String verification2 = verifyNewAssignment(assignment_f, newAssign_n_f, newAssign_w_f);
			if (verification2.equals("success")) {
				for(Assignment a : classRoom.getAssignments()) {
					classRoom.editAssignmentWeighting(a.getName(), Integer.parseInt(assignment_f.get(classRoom.getAssignments().indexOf(a)).getText()));
				}
				Assignment newAssign = new Assignment(Integer.parseInt(newAssign_w_f.getText()), newAssign_n_f.getText());
				classRoom.addAssignment(newAssign);
				classRoom.applyChanges();
				String msg = "Assignment: " + newAssign_n_f.getText() + " was successfully created!";
				PopupWindow popup = new PopupWindow("success", msg);
				window.setScene(new AssignmentManager(classRoom, back, window).getScene());
			} else {
				PopupWindow popup = new PopupWindow("error", verification2);
			}
		});
		
		for (Button b : assignment_b) {
			b.setOnAction(e-> {
				String verification3 = verifyWeightingWithExclusion(assignment_f, assignment_f.get(assignment_b.indexOf(b)));
				if (verification3.equals("success")) {
					classRoom.delAssignment(classRoom.getAssignments().get(assignment_b.indexOf(b)));
					classRoom.applyChanges();
					PopupWindow popup = new PopupWindow ("Success", "Assignment successfully removed");
					window.setScene(new AssignmentManager(classRoom, back, window).getScene());
				} else {
					PopupWindow popup = new PopupWindow("Error", verification3);
				}
			});
		}
			
	}
	
	/**
	 * Makes sure that the fields have the necessary data inside
	 * @param assignment_f
	 * @return whether the checkw as successful or not, if not, the error
	 */
	public String verifyFields(ArrayList<TextField> assignment_f) {
		for (TextField tf : assignment_f) {
			if (tf.getText() == null || tf.getText().equals("")) {
				return "A field is empty";
			}
			if (!tf.getText().matches("[0-9]+")) {
				return "Only numbers allowed in the fields";
			}
		}
		return "success";
	}
	
	/**
	 * Makes sure that the weighting in the assignment all equal 100 and the fields are verified
	 * @param assignment_f the textfields to be checked
	 * @return whether it was successful, or an error statement
	 */
	public String verifyWeightings(ArrayList<TextField> assignment_f) {
		
		String fieldVerification = verifyFields(assignment_f);
		if (!fieldVerification.equals("success")) {
			return fieldVerification;
		}
		
		int totalweighting = 0;
		for (TextField tf : assignment_f) {
			totalweighting += Integer.parseInt(tf.getText());
		}
		if (totalweighting != 100) {
			return "Weightings do not equal 100";
		}
		
		return "success";
	}
	
	/**
	 * Verifys all the weightings (weightings = 100) and fields except 1 textfield
	 * @param assignment_f the textfields to be verified
	 * @param exclusion the textfield to be ignored
	 * @return whether it was successful or not or an error code
	 */
	public String verifyWeightingWithExclusion(ArrayList<TextField> assignment_f, TextField exclusion) {
		
		String fieldVerification = verifyFields(assignment_f);
		if (!fieldVerification.equals("success")) {
			return fieldVerification;
		}
		
		int totalweighting = 0;
		for (TextField tf : assignment_f) {
			if (tf != exclusion) {
				totalweighting += Integer.parseInt(tf.getText());
			}
		}
		if (totalweighting != 100) {
			return "Weightings do not equal 100";
		}
		
		return "success";
	}
	
	/**
	 * Creates a new assignment and makes sure that the textfields are all ok and 
	 * the weightings = 100
	 * @param assignment_f the existing assignment textfields
	 * @param name the name of the new assignment
	 * @param weight the weight of the new assignment
	 * @return whether it was successful or not or an error code
	 */
	public String verifyNewAssignment(ArrayList<TextField> assignment_f, TextField name, TextField weight) {
		
		String fieldVerification = verifyFields(assignment_f);
		if (!fieldVerification.equals("success")) {
			return fieldVerification;
		}
		if (name.getText() == null || name.getText().equals("")) {
			return "Assignment name field is empty";
		}
		if (weight.getText() == null || weight.getText().equals("")) {
			return "Weight field is empty";
		}
		if (!weight.getText().matches("[0-9]+")) {
			return "Weight field can only contain numbers";
		}
		
		int totalweighting = 0;
		for (TextField tf : assignment_f) {
			totalweighting += Integer.parseInt(tf.getText());
		}
		totalweighting += Integer.parseInt(weight.getText());
		if (totalweighting != 100) {
			return "Weightings do not equal 100";
		}
		
		return "success";
	}
	
	/**
	 * 
	 * @return the scene of the interface
	 */
	public Scene getScene() {
		return scene;
	}
	
}
