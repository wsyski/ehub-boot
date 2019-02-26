package com.axiell.ehub.test;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement(name = "testData")
@XmlAccessorType(XmlAccessType.NONE)
public class TestDataDTO {
    private Long ehubConsumerId;
    private String ehubConsumerSecretKey;
    private long ehubLoanId;
    private String patronId;
    private String libraryCard;
    private String pin;
    private String email;
    private String name;
    private LocalDate birthDate;

    protected TestDataDTO() {
    }

    public TestDataDTO(final long ehubConsumerId, final String ehubConsumerSecretKey, final long ehubLoanId, final String patronId, final String libraryCard,
                       final String pin, final String email, final String name, final LocalDate birthDate) {
        this.ehubConsumerId = ehubConsumerId;
        this.ehubConsumerSecretKey = ehubConsumerSecretKey;
        this.ehubLoanId = ehubLoanId;
        this.patronId = patronId;
        this.libraryCard = libraryCard;
        this.pin = pin;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
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
    public long getEhubLoanId() {
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

    @XmlAttribute(name = "email", required = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "birthDate")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
        public LocalDate unmarshal(String v) {
            return LocalDate.parse(v);
        }

        public String marshal(LocalDate v) {
            return v.toString();
        }
    }
}


