import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The view of a teacher and see all their classes and they can create a new class
 * @author kymed
 *
 */

public class TeacherView {
	
	String teacher; // the teacher
	Stage window; // the window for the application
	Scene back, scene; // the scene to return too and the interface
	ClassList classList; // their list of classes
	
	/**
	 * Create the interface and handle the events
	 * @param classList
	 * @param window
	 * @param back
	 */
	public TeacherView(ClassList classList, Stage window, Scene back) {
		
		// Prepare Interface
		this.teacher = classList.getUser();
		this.window = window;
		this.back = back;
		
		// Generate Content
		Label title = new Label("Classes & Creator");
		
		ArrayList<Button> classes = new ArrayList<Button>();
		ArrayList<Label> courseAverages = new ArrayList<Label>();
		
		for (Classroom classRoom : classList.getClasses()) {
			classes.add(new Button(classRoom.getCode()));
			classes.get(classes.size() - 1).setOnAction(e->{
				window.setScene(new TeacherClassView(classRoom, scene, window).getScene());
			});
			String avg = Integer.toString(classRoom.getCourseAverage()) + "%";
			courseAverages.add(new Label(avg));
		}
		
		Label classname_l = new Label("Classname: ");
		TextField classname_f = new TextField("classname");
		
		Label classcode_l = new Label("Classcode: ");
		TextField classcode_f = new TextField("classcode");
		
		Button create_b = new Button("Create");
		Button logout_b = new Button("Logout");
		
		// Design Layout
		ScrollPane layout = new ScrollPane();
		VBox content = new VBox();
		
		GridPane grid = new GridPane();
		grid.add(classname_l, 2, 0);
		grid.add(classname_f, 3, 0);
		grid.add(classcode_l, 2, 1);
		grid.add(classcode_f, 3, 1);
		grid.add(create_b, 3, 2);
		
		content.setSpacing(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(20);
		grid.setVgap(10);
		
		for (int i = 0; i < classes.size(); i++) {
			grid.add(classes.get(i), 0, i);
			grid.add(courseAverages.get(i), 1, i);
		}
		grid.add(logout_b, 0, classes.size());
		
		content.getChildren().addAll(title, grid);
		layout.setContent(content);
		
		// Create Interface
		scene = new Scene(layout, 800, 600);
		window.setScene(scene);
		
		// Handle Events
		create_b.setOnAction(e -> {
			if (!classcode_f.getText().equals("") && !classname_f.getText().equals("")) {
				if (Controller.findClass(classcode_f.getText()) == null) {
					Classroom newClass = new Classroom(classcode_f.getText(), classname_f.getText(), classList.getUser(), new ArrayList<String>(), new ArrayList<String>());
					String addClass = Controller.addClass(newClass);
					if (addClass.equals("Class created successfully")) {
						classList.applyChanges();
						classList.resetInterface();
					} else {
						PopupWindow popup = new PopupWindow("error", addClass);
					}
				} else {
					PopupWindow popup = new PopupWindow("error", "Class already exists");
				}
			} else {
				PopupWindow popup = new PopupWindow("error", "fields are empty");
			}
		});
		
		
		
		logout_b.setOnAction(e->{
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
