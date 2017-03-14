package inventory;

import inventory.model.*;
import inventory.view.*;

import java.io.IOException;

import javafx.application.*;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author ceklov
 */

public class InventoryMain extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
//CE: ObservableList<> types rather than UML diagram's ArrayList<> types
    
    static ObservableList<Part> partsData = FXCollections.observableArrayList();
    static ObservableList<Product> productsData = FXCollections.observableArrayList();
        
/*CE: methods modifying ObservableLists, which are static (just like the ObservableLists themselves)
*because there is only one list for each type, and therefore
*no need to attach the variables to an instance of this class*/   
    
    public static void addPartsData(Part part) {
        //CE: functionally equivalent to UML diagram's addPart() method--renamed because it refers to static variable partsData
        partsData.add(part);
    } 
    
    public static void deletePartsData(Part part) { 
        //CE: this method is necessary when modifying the type (inhouse or outsourced) of a part
        //CE: functionally equivalent to UML diagram's removePart() method
        partsData.remove(part);
    }
        
    public static void addProductsData(Product product) {
        //CE: functionally equivalent to UML diagram's addProduct() method--renamed because it refers to static variable productsData
        productsData.add(product);
    }

//CE: getters for ObservableLists
    
    public ObservableList<Product> getProductsData() {
        return productsData;
    }
    
    public ObservableList<Part> getPartsData() {
        return partsData;
    }
    
//CE: search methods
    
    public static Part lookupPart(int partID) {
        //CE: create a new part only to be returned, NOT added to partsData
        Part partToReturn = null;
        
        for (Part part : partsData) {
            
            if (part.getPartID() == partID) { 
                
                if (part.isInhouse()) { 
                    partToReturn = new InhousePart();
                    ((InhousePart) partToReturn).setMachineID(((InhousePart) part).getMachineID());
                } else { //CE: it is an outsourced part
                    partToReturn = new OutsourcedPart();
                    ((OutsourcedPart) partToReturn).setCompanyName(((OutsourcedPart) part).getCompanyName());
                } //CE end if-else
            
                partToReturn.setPartID(part.getPartID());
                partToReturn.setPartName(part.getPartName());
                partToReturn.setPartPrice(part.getPartPrice());
                partToReturn.setPartStock(part.getPartStock());
                partToReturn.setPartMin(part.getPartMin());
                partToReturn.setPartMax(part.getPartMax());
                
                break;
            } //CE: end outer if
            
        } //CE: end for
        
        return partToReturn;
    }
    
    public static Product lookupProduct(int productID) {
        //CE: new product created only to be returned, NOT added to productsData
        Product productToReturn = null; 
        
        for (Product product : productsData) {
            
            if (product.getProductID() == productID) {
                productToReturn = new Product();
                
                productToReturn.setProductID(product.getProductID());
                productToReturn.setProductName(product.getProductName());
                productToReturn.setProductPrice(product.getProductPrice());
                productToReturn.setProductStock(product.getProductStock());
                productToReturn.setProductMin(product.getProductMin());
                productToReturn.setProductMax(product.getProductMax());
                productToReturn.setPartsInProduct(product.getPartsInProduct());
                
                break;
            } //CE: end if
            
        } //CE: end for
        
        return productToReturn;
    }

//CE: methods that load and show the various screens (or assist in doing so)
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
        
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Inventory Application");
        startRootLayout();
        showMainScreen();
    }
    
    public void startRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryMain.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryMain.class.getResource("view/MainScreen.fxml"));
            AnchorPane mainScreen = (AnchorPane) loader.load();
            rootLayout.setCenter(mainScreen);
            MainScreenController controller = loader.getController();
            controller.setInventoryMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddPart() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryMain.class.getResource("view/AddPart.fxml"));
            AnchorPane addPartPane = (AnchorPane) loader.load();
            
            Stage addPartStage = new Stage();
            addPartStage.setTitle("Add Part");
            addPartStage.initModality(Modality.WINDOW_MODAL);
            addPartStage.initOwner(primaryStage);
            
            Scene scene = new Scene(addPartPane);
            addPartStage.setScene(scene);
            
            AddPartController controller = loader.getController();
            controller.setAddPartStage(addPartStage);
            
            addPartStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showModifyPart(Part part) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryMain.class.getResource("view/ModifyPart.fxml"));
            AnchorPane modifyPartPane = (AnchorPane) loader.load();
            
            Stage modifyPartStage = new Stage();
            modifyPartStage.setTitle("Modify Part");
            modifyPartStage.initModality(Modality.WINDOW_MODAL);
            modifyPartStage.initOwner(primaryStage);
            
            Scene scene = new Scene(modifyPartPane);
            modifyPartStage.setScene(scene);
            
            ModifyPartController controller = loader.getController();
            controller.setModifyPartStage(modifyPartStage);
            controller.setPartsInCommon(part);
            
            modifyPartStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryMain.class.getResource("view/AddProduct.fxml"));
            AnchorPane addProductPane = (AnchorPane) loader.load();
            
            Stage addProductStage = new Stage();
            addProductStage.setTitle("Add Product");
            addProductStage.initModality(Modality.WINDOW_MODAL);
            addProductStage.initOwner(primaryStage);
            
            Scene scene = new Scene(addProductPane);
            addProductStage.setScene(scene);
            
            AddProductController controller = loader.getController();
            controller.setAddProductStage(addProductStage);
            controller.setAddProductPartsInventory(this);
                                   
            addProductStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showModifyProduct(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryMain.class.getResource("view/ModifyProduct.fxml"));
            AnchorPane modifyProductPane = (AnchorPane) loader.load();
            
            Stage modifyProductStage = new Stage();
            modifyProductStage.setTitle("Modify Product");
            modifyProductStage.initModality(Modality.WINDOW_MODAL);
            modifyProductStage.initOwner(primaryStage);
            
            Scene scene = new Scene(modifyProductPane);
            modifyProductStage.setScene(scene);
            
            ModifyProductController controller = loader.getController();
            controller.setModifyProductStage(modifyProductStage);
            controller.setProduct(product);
            controller.setModifyProductPartsInventory(this);
                                   
            modifyProductStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void exit() {
        primaryStage.close();
    }
    
    public static void main(String[] args) {
        launch(args);
        
    } 
    
}