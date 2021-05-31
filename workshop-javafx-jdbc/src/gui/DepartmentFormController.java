package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private DepartmentService departmentService;
	
	private Department entity;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		departmentService = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
			
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (departmentService == null) {
			throw new IllegalStateException("There is no service.");
		}
		if (entity == null) {
			throw new IllegalStateException("Entity is null.");
		}
		
		try {
			entity = getFormData();
			departmentService.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
		
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	public Department getFormData() {
		Department department = new Department();
		department.setId(Utils.tryParseToInt(textFieldId.getText()));
		department.setName(textFieldName.getText());
		return department;
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent e) {
		Utils.currentStage(e).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeNodes();
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 60);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity is null.");
		}
		textFieldId.setText(String.valueOf(entity.getId()));
		textFieldName.setText(entity.getName());
	}
	


}
