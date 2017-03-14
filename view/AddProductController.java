package inventory.view;

import inventory.*;
import inventory.model.*;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author ceklov
 */
public class AddProductController {
    private InventoryMain inventoryMain;
    
    private ObservableList<Part> tempPartsInProduct = FXCollections.observableArrayList();
    private Product tempProduct;
    
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
    
    private Stage addProductStage;
        
    @FXML private void initialize() {
        inventoryIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty());
        inventoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        inventoryInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().partStockProperty());
        inventoryCostColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty());
        
        partsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty());
        partsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        partsInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().partStockProperty());
        partsCostColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty());
     
        productIDField.setText(Integer.toString(Product.getProductCounter()));
    }
    
    public void setAddProductStage(Stage addProductStage) {
        this.addProductStage = addProductStage;
    }

    public void setAddProductPartsInventory(InventoryMain inventoryMain) {
        this.inventoryMain = inventoryMain;
        partsInventory.setItems(inventoryMain.getPartsData());
    }
    
    private void showPartsInProductTable() {
        partsInProduct.setItems(tempPartsInProduct);
    }
    
//CE: button methods
    
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

                Product product = new Product();

                product.setProductID();
                product.setProductName(productNameField.getText());
                product.setProductStock(Integer.parseInt(productInventoryField.getText()));
                product.setProductPrice(Double.parseDouble(productCostField.getText()));
                product.setProductMin(Integer.parseInt(productMinField.getText()));
                product.setProductMax(Integer.parseInt(productMaxField.getText()));
                product.setPartsInProduct(tempPartsInProduct);

                InventoryMain.addProductsData(product);
                addProductStage.close();
            } //CE: end inner if
        } //CE: end outer if
    } //CE: end handleSave()
    
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
        if ((tempPartsInProduct == null) || (tempPartsInProduct.size() == 0)) {
            errorMessage += "Product contains no parts\n";
        }
        
        /*CE: the following if-statements will not work if any of the above generated error messages--
        *so check if any problems thus far*/
        if (errorMessage.length() > 0) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid product information");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            errorMessage = "";
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
            addProductStage.close();
        }
    }
    
}
