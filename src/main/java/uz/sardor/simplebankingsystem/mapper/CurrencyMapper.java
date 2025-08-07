package uz.sardor.simplebankingsystem.mapper;

import org.springframework.stereotype.Component;
import uz.sardor.simplebankingsystem.dto.currency.response.CurrencyResponse;
import uz.sardor.simplebankingsystem.entity.Currency;

@Component
public class CurrencyMapper {
    public CurrencyResponse toCurrencyResponse(Currency currency) {
        return CurrencyResponse.builder()
                .code(currency.getCode())
                .ccy(currency.getCurrencyCode())
                .ccyNmUz(currency.getNameUz())
                .ccyNmUzc(currency.getNameUzc())
                .ccyNmEn(currency.getNameEng())
                .ccyNmRu(currency.getNameRu())
                .rate(currency.getRate())
                .nominal(currency.getNominal())
                .diff(currency.getDifference())
                .date(currency.getDate().toString())
                .build();
    }
}
