package uz.sardor.simplebankingsystem.dto.currency.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyUpdateRequest {
    private BigDecimal rate;
}
