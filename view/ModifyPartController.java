package inventory.view;

import inventory.*;
import inventory.model.*;
import java.util.ArrayList;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author ceklov
 */
public class ModifyPartController {
    @FXML private TextField partIDField;
    @FXML private TextField partNameField;
    @FXML private TextField partInventoryField;
    @FXML private TextField partCostField;
    @FXML private TextField partMinField;
    @FXML private TextField partMaxField;

    @FXML private Label partInhouseOrOutsourcedLabel;
    @FXML private TextField partInhouseOrOutsourcedField;
    
    @FXML private RadioButton partInhouseRadio;
    @FXML private RadioButton partOutsourcedRadio;
    
    private boolean isInhouse = true;
    
    private Stage modifyPartStage;
    private Part part;
    
    private void initialize() {
        
    }
     
    public void setModifyPartStage(Stage modifyPartStage) {
        this.modifyPartStage = modifyPartStage;
    }
       
    public void setPartsInCommon(Part part) {
        this.part = part;
        
        partIDField.setText(Integer.toString(part.getPartID()));
        partNameField.setText(part.getPartName());
        partInventoryField.setText(Integer.toString(part.getPartStock()));
        partCostField.setText(Double.toString(part.getPartPrice()));
        partMinField.setText(Integer.toString(part.getPartMin()));
        partMaxField.setText(Integer.toString(part.getPartMax()));
        
        if (part instanceof InhousePart) {
            setPart(((InhousePart) part));
        } else {
            setPart(((OutsourcedPart) part));
        }
    }
    
    public void setPart(InhousePart part) {
        handleInhouse();
        partInhouseOrOutsourcedField.setText(Integer.toString(part.getMachineID()));
    }
    
    public void setPart(OutsourcedPart part) {
        handleOutsourced();
        partInhouseOrOutsourcedField.setText(part.getCompanyName());
    }
 
    @FXML private void handleInhouse() {
        isInhouse = true;
        partInhouseOrOutsourcedLabel.setText("Machine ID");
        partInhouseRadio.setSelected(true);
        partOutsourcedRadio.setSelected(false);

    }
    
    @FXML private void handleOutsourced() {
        isInhouse = false;
        partInhouseOrOutsourcedLabel.setText("Company Name");
        partOutsourcedRadio.setSelected(true);
        partInhouseRadio.setSelected(false);
    }  
    
    @FXML private void handleSave() {
      
        if (isInputValid()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to save?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                if (part instanceof InhousePart) {
                    if (isInhouse) { //CE: part was and still is inhouse
                        (((InhousePart) part)).setMachineID(Integer.parseInt(partInhouseOrOutsourcedField.getText()));
                    } else { //CE: part was inhouse, but user changed to outsourced
                        OutsourcedPart outsourcedPart = new OutsourcedPart();
                        outsourcedPart.setPartID(part.getPartID()); //CE: ensure newly created part has same ID
                        InventoryMain.deletePartsData(part); //CE: delete original part because part's type cannot be changed
                        
                        outsourcedPart.setCompanyName(partInhouseOrOutsourcedField.getText());
                        savePartsInCommon(outsourcedPart);

                        InventoryMain.addPartsData(outsourcedPart);
                        modifyPartStage.close();
                    }
                } else { //CE: part was outsourced
                    if (!isInhouse) { //CE: part was and still is outsourced
                        (((OutsourcedPart) part)).setCompanyName(partInhouseOrOutsourcedField.getText());
                    } else { //CE: part was outsourced, but user changed to inhouse
                        InhousePart inhousePart = new InhousePart();
                        inhousePart.setPartID(part.getPartID());
                        InventoryMain.deletePartsData(part);
                        
                        inhousePart.setMachineID(Integer.parseInt(partInhouseOrOutsourcedField.getText()));
                        savePartsInCommon(inhousePart);

                        InventoryMain.addPartsData(inhousePart);
                        modifyPartStage.close();
                    }
                }

                savePartsInCommon(part); //CE: will run for parts whose types were not changed

                modifyPartStage.close(); 
            }
        }
    }
     
    private void savePartsInCommon(Part part) {
        part.setPartName(partNameField.getText());
        part.setPartStock(Integer.parseInt(partInventoryField.getText()));
        part.setPartPrice(Double.parseDouble(partCostField.getText()));
        part.setPartMin(Integer.parseInt(partMinField.getText()));
        part.setPartMax(Integer.parseInt(partMaxField.getText()));
    }
    
    private boolean isInputValid() {
        
        String errorMessage = "";
        
        //CE: ensure strings partName is non-null and non-empty
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
        
        /*CE: the following if-statements will not work if any of the above generated error messages so
        *check for any problems thus far*/
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
        if ((partInhouseOrOutsourcedField.getText() == null) || (partInhouseOrOutsourcedField.getText().length() == 0)) {
            errorMessage += "Invalid machine ID or company name\n";
        } else if (isInhouse) { //CE: creating/modifying an InhousePart, which requires a machineID of type int
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
            modifyPartStage.close();
        }
    }
    
}
