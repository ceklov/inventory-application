package inventory.view;

import inventory.*;
import inventory.model.*;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author ceklov
 */

public class MainScreenController {
    private InventoryMain inventoryMain; //CE: reference to main application
    
    @FXML private TextField searchPartsField;
    @FXML private TextField searchProductsField;
    
    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, Number> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Number> partInventoryColumn;
    @FXML private TableColumn<Part, Number> partCostColumn;
  
    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Number> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Number> productInventoryColumn;
    @FXML private TableColumn<Product, Number> productCostColumn;
    
    public MainScreenController() {   
    }
    
    @FXML private void initialize() { //CE: automatically called after the FXML file has been loaded
        partIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty());
        partNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        partInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().partStockProperty());
        partCostColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty());
        
        productIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        productInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().productStockProperty());
        productCostColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty());
               
    }
    
    public void setInventoryMain(InventoryMain inventoryMain) {
        this.inventoryMain = inventoryMain;
        partsTable.setItems(inventoryMain.getPartsData());
        productsTable.setItems(inventoryMain.getProductsData());
    }

//CE: buttons for parts table
    
    @FXML private void handleAddPart() {
        inventoryMain.showAddPart();
    }
    
    @FXML private void handleModifyPart() {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        
        if (selectedPart == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(inventoryMain.getPrimaryStage());
            alert.setTitle("No part selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part from the table.");
            
            alert.showAndWait();
        } else inventoryMain.showModifyPart(selectedPart);
  
    }    
     
    @FXML private void handleDeletePart() {
        //CE: functionally equivalent to UML diagram's removePart() method
        int selectedIndex = partsTable.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this part?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                partsTable.getItems().remove(selectedIndex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No part selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part from the table");
            
            alert.showAndWait();
        }

    }
   
//CE: buttons for products table
    
    @FXML private void handleAddProduct() {
        inventoryMain.showAddProduct();
    }
    
    @FXML private void handleModifyProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        
        if (selectedProduct == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(inventoryMain.getPrimaryStage());
            alert.setTitle("No product selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product from the table.");
            
            alert.showAndWait();
        } else inventoryMain.showModifyProduct(selectedProduct);
    }
   
    @FXML private void handleDeleteProduct() {
        //CE: functionally equivalent to UML diagram's removeProduct() method
        int selectedIndex = productsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert selectionAlert = new Alert(Alert.AlertType.CONFIRMATION);
            selectionAlert.setTitle("Confirmation Dialog");
            selectionAlert.setHeaderText(null);
            selectionAlert.setContentText("Are you sure you want to delete this product?");

            Optional<ButtonType> selectionResult = selectionAlert.showAndWait();
            
            //CE: additional confirmation dialog for products that contain parts
            if (selectionResult.get() == ButtonType.OK) {
                Product tempProduct = productsTable.getSelectionModel().getSelectedItem();
                if (tempProduct.getPartsInProduct().size() > 0) {
                    Alert productAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    productAlert.setTitle("Confirmation Dialog");
                    productAlert.setHeaderText(null);
                    productAlert.setContentText("Parts are still assigned to this product\n\nReally delete it?");

                    Optional<ButtonType> productResult = productAlert.showAndWait();
                    if (productResult.get() == ButtonType.OK) {
                        productsTable.getItems().remove(selectedIndex);
                    }
                } else {
                    productsTable.getItems().remove(selectedIndex);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No product selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product from the table");
            
            alert.showAndWait();
        }
    }

//CE: others
    
    @FXML private void handleExit() {
        inventoryMain.exit();
    }
    
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
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Part found!");
            alert.setHeaderText(null);
            if (part.isInhouse()) {
                alert.setContentText("ID " + part.getPartID() + "\nName: " + part.getPartName() + "\nPrice: " + part.getPartPrice() + "\nInventory: " + part.getPartStock() + "\nMin: " + part.getPartMin() + "\nMax: " + part.getPartMax() + "\nMachine ID: " + ((InhousePart) part).getMachineID());
            } else {
                alert.setContentText("ID " + part.getPartID() + "\nName: " + part.getPartName() + "\nPrice: " + part.getPartPrice() + "\nInventory: " + part.getPartStock() + "\nMin: " + part.getPartMin() + "\nMax: " + part.getPartMax() + "\nCompany Name: " + ((OutsourcedPart) part).getCompanyName());
            }

            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error(s) encountered!");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
        }
    }
    
    @FXML private void handleSearchProducts() {
        String errorMessage = "";
        Product product = null;
        
        if ((searchProductsField.getText() == null) || (searchProductsField.getText().length() == 0)) {
            errorMessage += "No product ID entered for search\n";
        } else {
            try {
                product = InventoryMain.lookupProduct(Integer.parseInt(searchProductsField.getText()));
            } catch (NumberFormatException e) {
                errorMessage += "Invalid product ID entered\n";
            }
        }
        
        if (product == null) {
            errorMessage += "No product matched the entered ID";
        }
        
        if (errorMessage.length() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Product found!");
            alert.setHeaderText(null);
            alert.setContentText("ID " + product.getProductID() + "\nName: " + product.getProductName() + "\nPrice: " + product.getProductPrice() + "\nInventory: " + product.getProductStock() + "\nMin: " + product.getProductMin() + "\nMax: " + product.getProductMax() + "\nParts " + product.getPartsInProduct());

            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error(s) encountered!");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
        }
    }
    
}
