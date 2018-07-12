import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Interface for creating a new account
 * @author kymed
 *
 */
public class CreateAccountInterface {
	
	Scene scene; // the interface
	
	/**
	 * Create the interface for creating an account
	 * @param window the window for the application
	 * @param back the scene to return too
	 */
	public CreateAccountInterface(Stage window, Scene back) {
		
		// Create Content
		Label title = new Label("New Account");
		Label subtitle = new Label ("Data cannot have: ',', ? or =");
		
		Label fullname_l = new Label("Full Name: ");
		TextField fullname_f = new TextField("fullname");
		Label username_l = new Label("Username: ");
		TextField username_f = new TextField("username");
		Label password_l = new Label("Password: ");
		PasswordField password_f = new PasswordField();
		Label confirmp_l = new Label("Confirm P: ");
		PasswordField confirmp_f = new PasswordField();
		
		RadioButton teacher_b = new RadioButton();
		teacher_b.setText("teacher");
		RadioButton student_b = new RadioButton();
		student_b.setText("student");
		
		ToggleGroup userType = new ToggleGroup();
		teacher_b.setToggleGroup(userType);
		student_b.setToggleGroup(userType);
		teacher_b.setSelected(true);
		
		Button submit = new Button("Submit");
		Button ret_urn = new Button("Return");
		
		// Create layout
		VBox layout = new VBox();
		
		HBox fullname_box = new HBox();
		HBox username_box = new HBox();
		HBox password_box = new HBox();
		HBox confirmp_box = new HBox();
		HBox userType_box = new HBox();
		HBox buttons_box = new HBox();
		
		fullname_box.getChildren().addAll(fullname_l, fullname_f);
		username_box.getChildren().addAll(username_l, username_f);
		password_box.getChildren().addAll(password_l, password_f);
		confirmp_box.getChildren().addAll(confirmp_l, confirmp_f);
		userType_box.getChildren().addAll(teacher_b, student_b);
		buttons_box.getChildren().addAll(submit, ret_urn);
		
		layout.getChildren().addAll(title, subtitle, fullname_box, username_box, password_box,
									confirmp_box, userType_box, buttons_box);
		layout.setAlignment(Pos.CENTER);
		fullname_box.setAlignment(Pos.CENTER);
		username_box.setAlignment(Pos.CENTER);
		password_box.setAlignment(Pos.CENTER);
		confirmp_box.setAlignment(Pos.CENTER);
		userType_box.setAlignment(Pos.CENTER);
		buttons_box.setAlignment(Pos.CENTER);
		
		
		// Set to the window
		scene = new Scene(layout, 800, 600);
		window.setScene(scene);
		
		// Handle Events
		ret_urn.setOnAction(e -> window.setScene(back));
		submit.setOnAction(e->{
			String fullname = fullname_f.getCharacters().toString();
			String username = username_f.getCharacters().toString();
			String password = password_f.getCharacters().toString();
			String confirmp = confirmp_f.getCharacters().toString();
			
			boolean isTeacher = teacher_b.isSelected();
			
			String attempt = checkData(fullname, username, password, confirmp);
			if (attempt.equals("data is successful")) {
				Controller.createUser(username, fullname, password, isTeacher);
				PopupWindow popup = new PopupWindow("Success", "Account Successfully Created");
				window.setScene(back);
			} else {
				PopupWindow popup = new PopupWindow("Error", attempt);
			}
		});
	}
	
	/**
	 * Verifies the data before creating an account, returns whether or not it was successful or if there was a problem
	 * @param fullname
	 * @param username
	 * @param password
	 * @param cpassword
	 * @return return statement
	 */
	public String checkData(String fullname, String username, String password, String cpassword) {
		if (!password.equals(cpassword)) {
			return "confirm password is not the same as password";
		}
		if (fullname.contains(",") || fullname.contains("?") || fullname.contains("=")) {
			return "full name contains an illegal character";
		}
		if (username.contains(",") || username.contains("?") || username.contains("=")) {
			return "Username contains an illegal character";
		}
		if (password.contains(",") || password.contains("?") || password.contains("=")) {
			return "Password contains an illegal character";
		}
		if (Controller.hasName(username)) {
			return "username already exists.";
		}
		
		return "data is successful";
	}
	
	/**
	 * Get the scene of the interface
	 * @return the scene
	 */
	public Scene getScene() {
		return scene;
	}
}
