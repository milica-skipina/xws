package orders.ordersmicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import orders.ordersmicroservice.model.Pricelist;

import java.util.Date;

@Getter @Setter
public class AdvertisementDTO {

    private Long id;
    private CarOrderDTO carAd;
    private Date startDate;
    private Date endDate;
    private String city;
    private String make;
    private String model;
    private double rating;
    private PricelistDTO pricelist;
    private String entrepreneur;        //name
    private String customer;

    public AdvertisementDTO(Long id, CarOrderDTO carAd, Date startDate, Date endDate, String city, String make,
                            String model, double rating, PricelistDTO pricelist, String entrepreneur, String customer) {
        this.id = id;
        this.carAd = carAd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.make = make;
        this.model = model;
        this.rating = rating;
        this.pricelist = pricelist;
        this.entrepreneur = entrepreneur;
        this.customer = customer;
    }

    public AdvertisementDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarOrderDTO getCarAd() {
        return carAd;
    }

    public void setCarAd(CarOrderDTO carAd) {
        this.carAd = carAd;
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

    public PricelistDTO getPricelist() {
        return pricelist;
    }

    public void setPricelist(PricelistDTO pricelist) {
        this.pricelist = pricelist;
    }


}
