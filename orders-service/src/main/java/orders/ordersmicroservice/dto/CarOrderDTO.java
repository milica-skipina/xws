package orders.ordersmicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import orders.ordersmicroservice.model.Car;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

@Getter @Setter
public class CarOrderDTO implements Serializable {
    private String make;
    private String model;
    private String fuel;
    private String carClass;
    private Boolean insurance;
    private Double mileage;
    private Double mileageLimit;
    private Double raiting;
    private String state;
    private String gearbox;
    private String entrepreneurUsername;    // username onoga ko je postavio oglas, moze to biti i kupac
    private String entrepreneurName;
    private String image;
    private Long id;        // ovo je microId, iz advertisementa

    public CarOrderDTO() {
    }

    public CarOrderDTO(String make, String model, String fuel, String carClass, Boolean insurance,
                       Double mileage, Double mileageLimit, Double raiting, String state, String image) {
        this.make = make;
        this.model = model;
        this.fuel = fuel;
        this.carClass = carClass;
        this.insurance = insurance;
        this.mileage = mileage;
        this.mileageLimit = mileageLimit;
        this.raiting = raiting;
        this.state = state;
        this.image = image;
    }

    public CarOrderDTO(Car c) {
        this.make = c.getMake();
        this.model = c.getModel();
        this.fuel = c.getFuel();
        this.carClass = c.getCarClass();
        this.insurance = c.isInsurance();
        this.mileage = c.getMileage();
        this.mileageLimit = c.getMileageLimit();
        this.raiting = c.getRaiting();
        this.state = c.getState();
        this.gearbox = c.getGearbox();
        this.entrepreneurUsername = c.getEntrepreneurUsername();
        this.image = c.getImage();
        this.id = c.getId();
    }

    public static CarOrderDTO fromBytes(byte[] body) {
        CarOrderDTO obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (body);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = (CarOrderDTO) ois.readObject();
            ois.close();
            bis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getMileageLimit() {
        return mileageLimit;
    }

    public void setMileageLimit(Double mileageLimit) {
        this.mileageLimit = mileageLimit;
    }

    public Double getRaiting() {
        return raiting;
    }

    public void setRaiting(Double raiting) {
        this.raiting = raiting;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getEntrepreneurUsername() {
        return entrepreneurUsername;
    }

    public void setEntrepreneurUsername(String entrepreneurUsername) {
        this.entrepreneurUsername = entrepreneurUsername;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEntrepreneurName() {
        return entrepreneurName;
    }

    public void setEntrepreneurName(String entrepreneurName) {
        this.entrepreneurName = entrepreneurName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

