package com.example.luminorjava;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.print.attribute.standard.DateTimeAtCreation;
import java.math.BigDecimal;
import java.util.Date;
import com.opencsv.bean.CsvBindByName;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @CsvBindByName(column = "amount", required = true)
    private BigDecimal amount;
    @CsvBindByName(column = "debtorIban", required = true)
    private String debtorIban;
    private Date createdAt;
    private String country;


    public Payment() {
    }

    public Payment(BigDecimal amount, String debtorIban) {
        this.amount = amount;
        this.debtorIban = debtorIban;
        this.createdAt = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDebtorIban() {
        return debtorIban;
    }

    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                ", debtorIban='" + debtorIban + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
