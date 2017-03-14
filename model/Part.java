package inventory.model;

import inventory.InventoryMain;
import inventory.view.MainScreenController;
import javafx.beans.property.*;
import javafx.scene.control.Alert;

/**
 *
 * @author ceklov
 */

public abstract class Part {
    //CE: using properties rather than primitives because of ObservableList type for partsData, partsInProduct, and productsData
    IntegerProperty partID;
    StringProperty partName;
    DoubleProperty partPrice;
    IntegerProperty partStock;
    IntegerProperty partMin;
    IntegerProperty partMax;
    
    /*CE: partCounter is static to easily access the variable without requiring an instantiated part--
    *this is used to ensure that each part ID will be unique, although deletion of parts will result in
    *the appearance of "skippeds" IDs*/
    static int partCounter = 1;
    
    private InventoryMain inventoryMain;
          
//CE: setters follow
    
    public abstract boolean isInhouse();
    
    public void setPartName(String partName) {
        this.partName.set(partName);
   }
   
   public void setPartID() {
       this.partID.set(partCounter++);
   }
   
   public void setPartID(int partID) {
       /*CE: this method is necessary when changing a part from inhouse to outsourced, or vice versa,
       *by using the same partID of the old part for the newly created part
       *as well as in the lookupParts() method*/
       this.partID.set(partID);
   }
   
   public void setPartPrice(double partPrice) {
       this.partPrice.set(partPrice);
   }
   
   public void setPartStock(int partStock) {
       this.partStock.set(partStock);
   }
   
   public void setPartMin(int partMin) {
       this.partMin.set(partMin);
   }
    
    public void setPartMax(int partMax) {
       this.partMax.set(partMax);
   }
   
//CE: getters follow
    
    public String getPartName() {
        return partName.get();
    }
    
    public StringProperty partNameProperty() {
        return partName;
    }
    
    public static int getPartCounter() {
        //CE: this method is used when automatically populating a part ID on the Add Part screen
        return partCounter;
    }
    
    public int getPartID() {
        return partID.get();
    }
    
    public IntegerProperty partIDProperty() {
        return partID;
    }
    
    public double getPartPrice() {
        return partPrice.get();
    }
    
    public DoubleProperty partPriceProperty() {
        return partPrice;
    }
    
    public int getPartStock() {
        return partStock.get();
    }
    
    public IntegerProperty partStockProperty() {
        return partStock;
    }
    
    public int getPartMin() {
        return partMin.get();
    }
    
    public IntegerProperty partMinProperty() {
        return partMin;
    }
    
    public int getPartMax() {
        return partMax.get();
    }
       
    public IntegerProperty partMaxProperty() {
        return partMax;
    }
}
