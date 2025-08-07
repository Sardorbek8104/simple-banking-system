package uz.sardor.simplebankingsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "currencies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Currency extends BaseEntity {

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "currency_code", unique = true)
    private String currencyCode;

    @Column(name = "name_uz", unique = true)
    private String nameUz;

    @Column(name = "name_uzc", unique = true)
    private String nameUzc;

    @Column(name = "name_eng", unique = true)
    private String nameEng;

    @Column(name = "name_ru", unique = true)
    private String nameRu;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "nominal")
    private String nominal;

    @Column(name = "difference")
    private BigDecimal difference;

    @Column(name = "date")
    private LocalDate date;
}