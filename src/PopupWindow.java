import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *  A  prioritized window for alerting the user with a message
 * @author kymed
 *
 */
public class PopupWindow {

	/**
	 * Create the window. Generate it's content. Create Modality and prioritize that
	 * the window has to be closed until the user can continue using the application.
	 * Create a layout for the content that's laid onto an interface that's laid onto
	 * the window of the application. Lay's content onto the layout and interface than
	 * runs the window.
	 * @param The title of the popup window
	 * @param The message that the window displays
	 */
	public PopupWindow(String title, String message) {
		
		// Create Window
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.UTILITY);
		window.setTitle(title);
		
		// Create Content
		Label label = new Label(message);
		Button ok_b = new Button("Ok");
		 
		// Create layout and integrate content
		VBox layout = new VBox();
		layout.setSpacing(50);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(label, ok_b);
		
		// Handle events
		ok_b.setOnAction(e -> window.close());
				
		// Create scene and initialize window
		Scene scene = new Scene(layout, 550, 250);
		window.setScene(scene);
		window.showAndWait();
	
		
	}
	
}
