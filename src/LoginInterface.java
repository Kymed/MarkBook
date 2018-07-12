import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The interface for logging in and the entry for the javafx application
 * @author kymed
 *
 */

public class LoginInterface extends Application {
	
	Stage window; // the window for the application
	
	/**
	 * Begins the javafx application and creates the first interface
	 */
	public void start(Stage stage) throws Exception{
		
		// Generate Content
		Label title = new Label("MarkBook");
		Label subtitle1 = new Label("By Kyle Meade");
		Label subtitle2 = new Label("Create/join classes & Mark students");
		
		Label username_l = new Label("Username: ");
		TextField username_f = new TextField("username");
		
		Label password_l = new Label("Password: ");
		PasswordField password_f = new PasswordField();
		
		Button createAcc_b = new Button("Create Account");
		Button submit_b = new Button("Submit");
		Button exit_b = new Button("Exit");
		
		// Design Layout
		VBox layout = new VBox();
		HBox username_box = new HBox();
		HBox password_box = new HBox();
		HBox buttons_box = new HBox();
		
		username_box.getChildren().addAll(username_l, username_f);
		password_box.getChildren().addAll(password_l, password_f);
		buttons_box.getChildren().addAll(createAcc_b, submit_b, exit_b);
		layout.getChildren().addAll(title, subtitle1, subtitle2, username_box, password_box, buttons_box);
		
		layout.setAlignment(Pos.CENTER);
		username_box.setAlignment(Pos.CENTER);
		password_box.setAlignment(Pos.CENTER);
		buttons_box.setAlignment(Pos.CENTER);
		buttons_box.setSpacing(10);
		
		// Initialize Window
		Scene scene = new Scene(layout, 800, 600);
		window = new Stage();
		window.setScene(scene);
		window.setTitle("MarkBook Application");
		
		window.show();
		
		// Handle Events
		exit_b.setOnAction(e -> Platform.exit());
		
		submit_b.setOnAction(e -> {
			String verification = verified(username_f.getText(), password_f.getText());
			if (verification.equals("success")) {
				ClassList proceed = new ClassList(username_f.getText(), Controller.getAccount(username_f.getText())[1], Controller.isTeacher(username_f.getText()), window, scene);
				username_f.setText("");
				password_f.setText("");
			} else {
				PopupWindow popup = new PopupWindow("error", verification);
			}
		});
		
		createAcc_b.setOnAction(e -> new CreateAccountInterface(window, scene).getScene());
		
	}
	
	/**
	 * Verifys the login information
	 * @param user
	 * @param pass
	 * @return whether or not it was successful
	 */
	public String verified(String user, String pass) {
		
		if (user.equals(null) || user.equals("")) {
			return "username empty";
		}
		
		if (pass.equals(null) || pass.equals("")) {
			return "password empty";
		}
		
		if (!Controller.hasName(user)) {
			return "Username not found";
		}
		
		if (!Controller.correctPass(user, pass)) {
			return "Incorrect Password";
		} 
		return "success";
	}
	
	/**
	 * Start the program
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
