//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.20 at 08:33:44 PM CEST 
//


package rs.ac.uns.ftn.xws_tim2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="mileage" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="kidsSeats" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="mileageLimit" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="pricelist" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="collisionDW" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "username",
    "id",
    "startDate",
    "endDate",
    "mileage",
    "kidsSeats",
    "mileageLimit",
    "pricelist",
    "city",
    "collisionDW"
})
@XmlRootElement(name = "editAdvertisementRequest")
public class EditAdvertisementRequest {

    @XmlElement(required = true)
    protected String username;
    protected long id;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endDate;
    protected double mileage;
    protected int kidsSeats;
    protected double mileageLimit;
    protected long pricelist;
    @XmlElement(required = true)
    protected String city;
    protected boolean collisionDW;

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the mileage property.
     * 
     */
    public double getMileage() {
        return mileage;
    }

    /**
     * Sets the value of the mileage property.
     * 
     */
    public void setMileage(double value) {
        this.mileage = value;
    }

    /**
     * Gets the value of the kidsSeats property.
     * 
     */
    public int getKidsSeats() {
        return kidsSeats;
    }

    /**
     * Sets the value of the kidsSeats property.
     * 
     */
    public void setKidsSeats(int value) {
        this.kidsSeats = value;
    }

    /**
     * Gets the value of the mileageLimit property.
     * 
     */
    public double getMileageLimit() {
        return mileageLimit;
    }

    /**
     * Sets the value of the mileageLimit property.
     * 
     */
    public void setMileageLimit(double value) {
        this.mileageLimit = value;
    }

    /**
     * Gets the value of the pricelist property.
     * 
     */
    public long getPricelist() {
        return pricelist;
    }

    /**
     * Sets the value of the pricelist property.
     * 
     */
    public void setPricelist(long value) {
        this.pricelist = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the collisionDW property.
     * 
     */
    public boolean isCollisionDW() {
        return collisionDW;
    }

    /**
     * Sets the value of the collisionDW property.
     * 
     */
    public void setCollisionDW(boolean value) {
        this.collisionDW = value;
    }

}
