package inventory.model;

import static inventory.model.Part.partCounter;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ceklov
 */

public class OutsourcedPart extends Part {
    private StringProperty companyName;
    
    public OutsourcedPart() {
        this(null, 0.0, 0, 0, 99, null);
    }
    
    public OutsourcedPart(String partName, double partPrice, int partStock, int partMin, int partMax, String companyName) {
        this.partName = new SimpleStringProperty(partName);
        this.partPrice = new SimpleDoubleProperty(partPrice);
        this.partStock = new SimpleIntegerProperty(partStock);
        this.partMin = new SimpleIntegerProperty(partMin);
        this.partMax = new SimpleIntegerProperty(partMax);
        this.companyName = new SimpleStringProperty(companyName);
        this.partID = new SimpleIntegerProperty(partCounter);
    }  

//CE: getters for outsourced parts
    
    @Override
    public boolean isInhouse() {
        return false;
    }
    
    public String getCompanyName() {
        return companyName.get();
    }
    
    public StringProperty companyNameProperty() {
        return companyName;
    }
    
//CE: setter
    
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
    
}
