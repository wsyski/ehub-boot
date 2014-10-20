package com.axiell.ehub.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testData")
@XmlAccessorType(XmlAccessType.NONE)
public class TestData {
    private Long ehubConsumerId;
    private String ehubConsumerSecretKey;
    private Long ehubLoanId;
    private String patronId;
    private String libraryCard;
    private String pin;

    protected TestData() {
    }

    public TestData(Long ehubConsumerId, String ehubConsumerSecretKey, Long ehubLoanId, String patronId, String libraryCard, String pin) {
        this.ehubConsumerId = ehubConsumerId;
        this.ehubConsumerSecretKey = ehubConsumerSecretKey;
        this.ehubLoanId = ehubLoanId;
        this.patronId = patronId;
        this.libraryCard = libraryCard;
        this.pin = pin;
    }

    @XmlAttribute(name = "ehubConsumerId", required = true)
    public Long getEhubConsumerId() {
        return ehubConsumerId;
    }

    public void setEhubConsumerId(Long ehubConsumerId) {
        this.ehubConsumerId = ehubConsumerId;
    }

    @XmlAttribute(name = "ehubConsumerSecretKey", required = true)
    public String getEhubConsumerSecretKey() {
        return ehubConsumerSecretKey;
    }

    public void setEhubConsumerSecretKey(String ehubConsumerSecretKey) {
        this.ehubConsumerSecretKey = ehubConsumerSecretKey;
    }

    @XmlAttribute(name = "ehubLoanId", required = true)
    public Long getEhubLoanId() {
        return ehubLoanId;
    }

    public void setEhubLoanId(Long ehubLoanId) {
        this.ehubLoanId = ehubLoanId;
    }

    @XmlAttribute(name = "patronId", required = true)
    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    @XmlAttribute(name = "libraryCard", required = true)
    public String getLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(String libraryCard) {
        this.libraryCard = libraryCard;
    }

    @XmlAttribute(name = "pin", required = true)
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "ehubConsumerId=" + ehubConsumerId +
                ", ehubConsumerSecretKey=" + ehubConsumerSecretKey +
                ", ehubLoanId=" + ehubLoanId +
                ", libraryCard=" + libraryCard +
                ", pin=" + pin +
                '}';
    }
}
