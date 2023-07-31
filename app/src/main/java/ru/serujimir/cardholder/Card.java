package ru.serujimir.cardholder;

public class Card {

    public Card(String number, String cvv, String expiration, String id) {
        this.number=number;
        this.cvv=cvv;
        this.expiration=expiration;
        this.id=id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String number;
    private String cvv;
    private String expiration;

    private String id;
}
