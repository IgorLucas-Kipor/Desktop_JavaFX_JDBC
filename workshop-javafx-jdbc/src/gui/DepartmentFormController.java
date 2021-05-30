package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {
	
	@FXML
	private TextField textFieldId;
	
	@FXML
	private TextField textFieldName;
	
	@FXML
	private Label errorLabel;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("Save.");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("Cancel.");
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeNodes();
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 60);
	}

}
