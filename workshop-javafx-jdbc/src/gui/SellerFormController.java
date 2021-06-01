package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private SellerService sellerService;

	private Seller entity;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldName;
	
	@FXML
	private TextField textFieldEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField textFieldBaseSalary;

	@FXML
	private Label errorLabel;
	
	@FXML
	private Label errorEmail;
	
	@FXML
	private Label errorBirthDate;
	
	@FXML
	private Label errorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setSellerService(SellerService service) {
		sellerService = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (sellerService == null) {
			throw new IllegalStateException("There is no service.");
		}
		if (entity == null) {
			throw new IllegalStateException("Entity is null.");
		}

		try {
			entity = getFormData();
			sellerService.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		} catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	public Seller getFormData() {
		ValidationException exception = new ValidationException("Validation Error.");
		Seller seller = new Seller();
		seller.setId(Utils.tryParseToInt(textFieldId.getText()));

		if (textFieldName.getText() == null || textFieldName.getText().trim().equals("")) {
			exception.addError("name", "field can't be empty");
		}
		seller.setName(textFieldName.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return seller;
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
		Constraints.setTextFieldDouble(textFieldBaseSalary);
		Constraints.setTextFieldMaxLength(textFieldEmail, 100);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity is null.");
		}
		textFieldId.setText(String.valueOf(entity.getId()));
		textFieldName.setText(entity.getName());
		textFieldEmail.setText(entity.getEmail());
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));			
		}
		Locale.setDefault(Locale.US);
		textFieldBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			errorLabel.setText(errors.get("name"));
		}
	}

}
