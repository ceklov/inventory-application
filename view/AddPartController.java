package inventory.view;

import inventory.*;
import inventory.model.*;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author ceklov
 */
public class AddPartController {

    @FXML private TextField partIDField;
    @FXML private TextField partNameField;
    @FXML private TextField partInventoryField;
    @FXML private TextField partCostField;
    @FXML private TextField partMinField;
    @FXML private TextField partMaxField;

    @FXML private RadioButton inhouseRadio;
    @FXML private RadioButton outsourcedRadio;
    
    @FXML private Label partInhouseOrOutsourcedLabel;
    @FXML private TextField partInhouseOrOutsourcedField;
    
    private boolean isInhouse = true;
    
    private Stage addPartStage;
        
    @FXML private void initialize() {
        partIDField.setText(Integer.toString(Part.getPartCounter()));
    }
    
    public void setAddPartStage(Stage addPartStage) {
        this.addPartStage = addPartStage;
    }

    @FXML private void handleInhouse() {
        isInhouse = true;
        partInhouseOrOutsourcedLabel.setText("Machine ID");
    }
    
    @FXML private void handleOutsourced() {
        isInhouse = false;
        partInhouseOrOutsourcedLabel.setText("Company Name");
    }
    
    @FXML private void handleSave() {

        if (isInputValid()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to save?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                if (isInhouse) {
                    InhousePart inhousePart = new InhousePart();
                    savePartsInCommon(inhousePart);
                    inhousePart.setMachineID(Integer.parseInt(partInhouseOrOutsourcedField.getText()));

                    InventoryMain.addPartsData(inhousePart);
                    addPartStage.close();
                } else {
                    OutsourcedPart outsourcedPart = new OutsourcedPart();
                    savePartsInCommon(outsourcedPart);
                    outsourcedPart.setCompanyName(partInhouseOrOutsourcedField.getText());

                    InventoryMain.addPartsData(outsourcedPart);
                    addPartStage.close();
                } //CE: end inner if-else
            } //CE: end middle if
        } // CE: end outer if
    } //CE: end handleSave()
    
    private void savePartsInCommon(Part part) {
        //CE: this helper method avoids some code redundancy, by using setters applicable to both InhouseParts and OutsourcedParts
        part.setPartID();
        part.setPartName(partNameField.getText());
        part.setPartStock(Integer.parseInt(partInventoryField.getText()));
        part.setPartPrice(Double.parseDouble(partCostField.getText()));
        part.setPartMin(Integer.parseInt(partMinField.getText()));
        part.setPartMax(Integer.parseInt(partMaxField.getText()));
    }
    
    private boolean isInputValid() {
            
        String errorMessage = "";
        
        //CE: ensure string partName is non-null and non-empty
        if ((partNameField.getText() == null) || (partNameField.getText().length() == 0)) {
            errorMessage += "Invalid part name\n";
        }

        //CE: ensure the contents of partsInventory, partMin, and partMax can be parsed as integers
        if ((partInventoryField.getText() == null) || (partInventoryField.getText().length() == 0)) {
            errorMessage += "Part inventory must contain a value\n";
        } else {
            try {
                Integer.parseInt(partInventoryField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid inventory--must be an integer\n";
            }
        }
        if ((partMinField.getText() == null) || (partMinField.getText().length() == 0)) {
            errorMessage += "Part minimum must contain a value\n";
        } else {
            try {
                Integer.parseInt(partMinField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid part minimum--must be an integer\n";
            }
        }
        if ((partMaxField.getText() == null) || (partMaxField.getText().length() == 0)) {
            errorMessage += "Part maximum must contain a value\n";
        } else {
            try {
                Integer.parseInt(partMaxField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid part maximum--must be an integer\n";
            }
        }
        
        //CE: ensure partCost is non-empty and non-null, and can be parsed as a double
        if ((partCostField.getText() == null) || (partCostField.getText().length() == 0)) {
            errorMessage += "Part cost must contain a value\n";
        } else {
            try {
                Double.parseDouble(partCostField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Part cost is invalid--must be a double\n";
            }        
        }
        
        /*CE: ensure partInhouseOrOutsourcedField is non-empty and non-null--
        *check to see if can be parsed as an integer after we check whether or not it is inhouse or outsourced*/
        if ((partInhouseOrOutsourcedField.getText() == null) || (partInhouseOrOutsourcedField.getText().length() == 0)) {
            errorMessage += "Invalid machine ID or company name\n";
        }
        
        /*CE: the following if-statements will not work if any of the above generated error messages--
        *so check if any errors thus far*/
        if (errorMessage.length() > 0) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid part information");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            errorMessage = "";
            return false;
        }
        
        //CE: ensure partMin is >= 0 and <= partMax
        if (Integer.parseInt(partMinField.getText()) < 0) {
            errorMessage += "Part minimum is below 0\n";
        }
        if (Integer.parseInt(partMinField.getText()) > Integer.parseInt(partMaxField.getText())) {
            errorMessage += "Part minimum exceeds part maximum\n";
        }
        
        //CE: ensure partInventory is >= partMin (and hence >= 0) and <= partMax
        if (Integer.parseInt(partInventoryField.getText()) < Integer.parseInt(partMinField.getText())) {
            errorMessage += "Inventory is below part minimum\n";
        }
        if (Integer.parseInt(partInventoryField.getText()) > Integer.parseInt(partMaxField.getText())) {
            errorMessage += "Inventory is above part maximum\n";
        }

        //CE: ensure machineID can be parsed as an integer, or companyName is non-null and non-empty
        if (isInhouse) {
            //CE: creating an InhousePart, which requires a machineID of type int
            try {
                Integer.parseInt(partInhouseOrOutsourcedField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Machine ID must be an integer";
            } //CE: end try-catch
        }
        
        //CE: final check if there were any problems
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid part information");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            errorMessage = "";
            return false;
        }
        
    }
    
    @FXML private void handleCancel() {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to leave this screen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            addPartStage.close();
        }
    }
    
}
