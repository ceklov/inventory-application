package inventory.model;

import static inventory.model.Part.partCounter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ceklov
 */

public class InhousePart extends Part {
    private IntegerProperty machineID;
    
    public InhousePart() {
        this(null, 0.0, 0, 0, 99, 0);
    }
    
    public InhousePart(String partName, double partPrice, int partStock, int partMin, int partMax, int machineID) {
        this.partName = new SimpleStringProperty(partName);
        this.partPrice = new SimpleDoubleProperty(partPrice);
        this.partStock = new SimpleIntegerProperty(partStock);
        this.partMin = new SimpleIntegerProperty(partMin);
        this.partMax = new SimpleIntegerProperty(partMax);
        this.machineID = new SimpleIntegerProperty(machineID);
        this.partID = new SimpleIntegerProperty(partCounter);
    }  
    
//CE: getters for inhouse parts

    @Override
    public boolean isInhouse() {
        return true;
    }
    
    public int getMachineID() {
        return machineID.get();
    }
    
    public IntegerProperty machineIDProperty() {
        return machineID;
    }
    
//CE: setter
    
    public void setMachineID(int machineID) {
        this.machineID.set(machineID);
    }
    
}