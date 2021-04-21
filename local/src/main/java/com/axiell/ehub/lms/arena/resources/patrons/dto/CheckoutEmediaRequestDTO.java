package com.axiell.ehub.lms.arena.resources.patrons.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CheckoutEmediaRequestDTO {
    private Boolean isLoanPerProduct;
    private LocalDate expirationDate;
    private String issue;

    public CheckoutEmediaRequestDTO() {
    }

    public CheckoutEmediaRequestDTO(final LocalDate expirationDate, final String issue, final Boolean isLoanPerProduct) {
        this.isLoanPerProduct = isLoanPerProduct;
        this.expirationDate = expirationDate;
        this.issue = issue;
    }

    @JsonProperty("isLoanPerProduct")
    public Boolean isLoanPerProduct() {
        return isLoanPerProduct;
    }
    @JsonProperty("isLoanPerProduct")
    public void setLoanPerProduct(final Boolean isLoanPerProduct) {
        this.isLoanPerProduct = isLoanPerProduct;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(final String issue) {
        this.issue = issue;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
