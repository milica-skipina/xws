package orders.ordersmicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import orders.ordersmicroservice.model.Advertisement;
import orders.ordersmicroservice.model.WishlistItem;

import java.util.Date;

@Getter @Setter
public class BasketDTO {
    private Long wishlistId;
    private Long advertisementId;
    private Date startDate;
    private Date endDate;
    private String city;
    private String make;
    private String model;
    private double price;
    private double rating;
    private String entrepreneur;        //name
    private String customer;            //name
    private CarOrderDTO car;
    private boolean isBundle;

    public BasketDTO() {
    }

    public BasketDTO(WishlistItem ad) {
        this.startDate = ad.getStartDate();
        this.endDate = ad.getEndDate();
        this.city = ad.getAdvertisement().getCity();
        this.make = ad.getAdvertisement().getCarAd().getMake();      // naziv proizvodjaca ?
        this.model = ad.getAdvertisement().getCarAd().getModel();
        this.rating = ad.getAdvertisement().getCarAd().getRaiting();
        this.entrepreneur = ad.getAgentName();
        this.advertisementId = ad.getAdvertisement().getId();
        this.wishlistId = ad.getId();
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(String entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public CarOrderDTO getCar() {
        return car;
    }

    public void setCar(CarOrderDTO car) {
        this.car = car;
    }
}


