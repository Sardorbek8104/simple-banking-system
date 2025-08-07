package uz.sardor.simplebankingsystem.dto.currency.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyFilterRequest {
    private String code;
    private String currencyCode;
    private String nameUz;
    private BigDecimal rateFrom;
    private BigDecimal rateTo;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}