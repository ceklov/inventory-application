package inventory.model;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ceklov
 */

public class Product {
    
//CE: the following rather than UML diagram's ArrayList<Part>
    private ObservableList<Part> partsInProduct = FXCollections.observableArrayList();
    
//CE: properties rather than primitive types, in conjunction with ObservableList
    private StringProperty productName;
    private IntegerProperty productID;
    private DoubleProperty productPrice;
    private IntegerProperty productStock;
    private IntegerProperty productMin;
    private IntegerProperty productMax;
    
    /*CE: productCounter is static to easily access the variable without requiring an instantiated product--
    *this is used to ensure that each product ID will be unique, although deletion of products will result in
    *the appearance of "skippeds" IDs*/
    private static int productCounter = 1;
    
    public Product() {
        this(null, 0.0, 0, 0, 99, null);
    }
    
    public Product(String productName, double productPrice, int productStock, int productMin, int productMax, ObservableList<Part> partsInProduct) {
        this.productName = new SimpleStringProperty(productName);
        this.productPrice = new SimpleDoubleProperty(productPrice);
        this.productStock = new SimpleIntegerProperty(productStock);
        this.productMin = new SimpleIntegerProperty(productMin);
        this.productMax = new SimpleIntegerProperty(productMax);
        this.partsInProduct = partsInProduct;
        this.productID = new SimpleIntegerProperty(productCounter);
    }
       
//CE: setters follow
    
    public void setPartsInProduct(ObservableList<Part> partsInProduct) {
        //CE: implement exception to not be empty
        this.partsInProduct = partsInProduct;
    }
    
    public void setProductID() {
        this.productID.set(productCounter++);
    }
    
    public void setProductID(int productID) {
        //CE: this method is necessary for the lookupProduct() method
        this.productID.set(productID);
    }
    
    public void setProductStock(int productStock) {
        this.productStock.set(productStock);
    }
    
    public void setProductMin(int productMin) {
        this.productMin.set(productMin);
    }
    
    public void setProductMax(int productMax) {
        this.productMax.set(productMax);
    }
    
    public void setProductName(String productName) {
        this.productName.set(productName);
    }
    
    public void setProductPrice(double productPrice) {
        this.productPrice.set(productPrice);
    }
    
//CE: getters follow
    
    public ObservableList<Part> getPartsInProduct() {
        return partsInProduct;
    }
    
    public static int getProductCounter() {
        //CE: this method is used when automatically populating a product ID on the Add Product screen
        return productCounter;
    }
    
    public int getProductID() {
        return productID.get();
    }
    
    public IntegerProperty productIDProperty() {
        return productID;
    }
    
    public int getProductStock() {
        return productStock.get();
    }
        
    public IntegerProperty productStockProperty() {
        return productStock;
    }
    
    public int getProductMin() {
        return productMin.get();
    }
    
    public IntegerProperty productMinProperty() {
        return productMin;
    }
    
    public int getProductMax() {
        return productMax.get();
    }
    
    public IntegerProperty productMaxProperty() {
        return productMax;
    }
        
    public String getProductName() {
        return productName.get();
    }
    
    public StringProperty productNameProperty() {
        return productName;
    }
    
    public double getProductPrice() {
        return productPrice.get();
    }
    
    public DoubleProperty productPriceProperty() {
        return productPrice;
    }
            
}