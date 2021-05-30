package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	public static Stage currentStage(ActionEvent e) {
		return (Stage) ((Node) e.getSource()).getScene().getWindow();
	}

}
