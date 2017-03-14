package inventory.view;

import inventory.*;
import inventory.model.*;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author ceklov
 */

public class ModifyProductController {
    private InventoryMain inventoryMain;

    private ObservableList<Part> tempPartsInProduct;
    private Product product;
    
    @FXML private TextField searchPartsField;
    
    @FXML private TextField productIDField;
    @FXML private TextField productNameField;
    @FXML private TextField productInventoryField;
    @FXML private TextField productCostField;
    @FXML private TextField productMinField;
    @FXML private TextField productMaxField;
    
    @FXML private TableView<Part> partsInventory; //CE: all available parts, automatically populated
    @FXML private TableColumn<Part, Number> inventoryIDColumn;
    @FXML private TableColumn<Part, String> inventoryNameColumn;
    @FXML private TableColumn<Part, Number> inventoryInventoryColumn;
    @FXML private TableColumn<Part, Number> inventoryCostColumn;
    
    @FXML private TableView<Part> partsInProduct; //CE: only those parts included in the given product
    @FXML private TableColumn<Part, Number> partsIDColumn;
    @FXML private TableColumn<Part, String> partsNameColumn;
    @FXML private TableColumn<Part, Number> partsInventoryColumn;
    @FXML private TableColumn<Part, Number> partsCostColumn;
    
    private Stage modifyProductStage;
        
    @FXML private void initialize() {
        inventoryIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty());
        inventoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        inventoryInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().partStockProperty());
        inventoryCostColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty());
        
        partsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty());
        partsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        partsInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().partStockProperty());
        partsCostColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty());
    
    }
    
    public void setModifyProductStage(Stage modifyProductStage) {
        this.modifyProductStage = modifyProductStage;
    }

    //CE: the following method populates the top table--all parts available
    public void setModifyProductPartsInventory(InventoryMain inventoryMain) {
        this.inventoryMain = inventoryMain;
        partsInventory.setItems(inventoryMain.getPartsData());
    }
    
    //CE: the following method populates the selected product's information, using a temporary ObservableList for its list of parts
    public void setProduct(Product product) {
        this.product = product;
        tempPartsInProduct = product.getPartsInProduct();
        productIDField.setText(Integer.toString(product.getProductID()));
        productNameField.setText(product.getProductName());
        productInventoryField.setText(Integer.toString(product.getProductStock()));
        productCostField.setText(Double.toString(product.getProductPrice()));
        productMinField.setText(Integer.toString(product.getProductMin()));
        productMaxField.setText(Integer.toString(product.getProductMax()));
        showPartsInProductTable();
    }
    
    private void showPartsInProductTable() {
        partsInProduct.setItems(tempPartsInProduct);
    }
    
//CE: button methods follow
    
    @FXML private void handleSearchParts() {
        String errorMessage = "";
        Part part = null;
        
        if ((searchPartsField.getText() == null) || (searchPartsField.getText().length() == 0)) {
            errorMessage += "No part ID entered for search\n";
        } else {
            try {
                part = InventoryMain.lookupPart(Integer.parseInt(searchPartsField.getText()));
            } catch (NumberFormatException e) {
                errorMessage += "Invalid part ID entered\n";
            }
        }
        
        if (part == null) {
            errorMessage += "No part matched the entered ID";
        }
        
        if (errorMessage.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part found!");
            alert.setHeaderText(null);
            if (part.isInhouse()) {
                alert.setContentText("ID " + part.getPartID() + "\nName: " + part.getPartName() + "\nPrice: " + part.getPartPrice() + "\nInventory: " + part.getPartStock() + "\nMin: " + part.getPartMin() + "\nMax: " + part.getPartMax() + "\nMachine ID: " + ((InhousePart) part).getMachineID());
            } else {
                alert.setContentText("ID " + part.getPartID() + "\nName: " + part.getPartName() + "\nPrice: " + part.getPartPrice() + "\nInventory: " + part.getPartStock() + "\nMin: " + part.getPartMin() + "\nMax: " + part.getPartMax() + "\nCompany Name: " + ((OutsourcedPart) part).getCompanyName());
            }

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error(s) encountered!");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
        }
    }

    //CE: functionally equivalent to UML diagram's updateProduct() method
    @FXML private void handleAdd() {
        Part selectedPart = partsInventory.getSelectionModel().getSelectedItem();
        
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(inventoryMain.getPrimaryStage());
            alert.setTitle("No part selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part from the upper table.");
            
            alert.showAndWait();
        } else {
            tempPartsInProduct.add(selectedPart);
            showPartsInProductTable();
        }
    }
    
    @FXML private void handleDelete() {
        /*CE: I have chosen NOT to implement a confirmation dialogue here because
        *doing so would simply be cumbersome as this button does not permanently change the object
        *and is easily reversed simply by adding the part again*/
        
        Part selectedPart = partsInProduct.getSelectionModel().getSelectedItem();
        
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(inventoryMain.getPrimaryStage());
            alert.setTitle("No part selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part from the lower table.");
            
            alert.showAndWait();
        } else {
            tempPartsInProduct.remove(selectedPart);
            showPartsInProductTable();
        }
    }
    
    @FXML private void handleSave() {

        if (isInputValid()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to save?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                product.setProductName(productNameField.getText());
                product.setProductStock(Integer.parseInt(productInventoryField.getText()));
                product.setProductPrice(Double.parseDouble(productCostField.getText()));
                product.setProductMin(Integer.parseInt(productMinField.getText()));
                product.setProductMax(Integer.parseInt(productMaxField.getText()));
                product.setPartsInProduct(tempPartsInProduct);

                modifyProductStage.close();
            }
        }
    }
    
    private boolean isInputValid() {
        
        String errorMessage = "";
        
        //CE: ensure strings productName is non-null and non-empty
        if ((productNameField.getText() == null) || (productNameField.getText().length() == 0)) {
            errorMessage += "Invalid product name\n";
        }

        //CE: ensure the contents of productsInventory, productMin, and productMax can be parsed as integers
        if ((productInventoryField.getText() == null) || (productInventoryField.getText().length() == 0)) {
            errorMessage += "Product inventory must contain a value\n";
        } else {
            try {
                Integer.parseInt(productInventoryField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid inventory--must be an integer\n";
            }
        }
        if ((productMinField.getText() == null) || (productMinField.getText().length() == 0)) {
            errorMessage += "Product minimum must contain a value\n";
        } else {
            try {
                Integer.parseInt(productMinField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid product minimum--must be an integer\n";
            }
        }
        if ((productMaxField.getText() == null) || (productMaxField.getText().length() == 0)) {
            errorMessage += "Product maximum must contain a value\n";
        } else {
            try {
                Integer.parseInt(productMaxField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid product maximum--must be an integer\n";
            }
        }
        
        //CE: ensure productCost is non-empty and non-null, and can be parsed as a double
        if ((productCostField.getText() == null) || (productCostField.getText().length() == 0)) {
            errorMessage += "Product cost must contain a value\n";
        } else {
            try {
                Double.parseDouble(productCostField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Product cost is invalid--must be a double\n";
            }        
        }
        
        //CE: ensure tempPartsInProduct contains at least one Part
        if ((tempPartsInProduct == null) || (tempPartsInProduct.isEmpty())) {
            errorMessage += "Product contains no parts\n";
        }
        
        /*CE: the following if-statements will not work if any of the above generated error messages--
        *so check for any problems thus far*/
        if (errorMessage.length() > 0) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid product information");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            errorMessage = ""; //CE: reset to empty, otherwise the error messages from previous save attempts could be repeated
            return false;
        }
        
        //CE: ensure productCost is >= total cost of included parts
        double costOfParts = 0;
        for (Part part : tempPartsInProduct) {
            costOfParts += part.getPartPrice();
        }
        if (Double.parseDouble(productCostField.getText()) < costOfParts) {
            errorMessage += "Product price is less than total cost of its constituent parts\n";
        }
        
        //CE: ensure productMin is >= 0 and <= productMax
        if (Integer.parseInt(productMinField.getText()) < 0) {
            errorMessage += "Product minimum is below 0\n";
        }
        if (Integer.parseInt(productMinField.getText()) > Integer.parseInt(productMaxField.getText())) {
            errorMessage += "Product minimum exceeds product maximum\n";
        }
        
        //CE: ensure productInventory is >= productMin (and hence >= 0) and <= productMax
        if (Integer.parseInt(productInventoryField.getText()) < Integer.parseInt(productMinField.getText())) {
            errorMessage += "Inventory is below product minimum\n";
        }
        if (Integer.parseInt(productInventoryField.getText()) > Integer.parseInt(productMaxField.getText())) {
            errorMessage += "Inventory is above product maximum\n";
        }
        
        //CE: final check before returning        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid product information");
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
            modifyProductStage.close();
        }
    }
    
}
