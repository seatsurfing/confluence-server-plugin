package de.seatsurfing.confluence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class Config {
  @XmlElement private String bookingUiUrl;
  @XmlElement private String sharedSecret;
        
  public String getBookingUiUrl() {
    return bookingUiUrl;
  }
        
  public void setBookingUiUrl(String bookingUiUrl) {
    this.bookingUiUrl = bookingUiUrl;
  }
        
  public String getSharedSecret() {
    return sharedSecret;
  }
        
  public void setSharedSecret(String sharedSecret) {
    this.sharedSecret = sharedSecret;
  }
}