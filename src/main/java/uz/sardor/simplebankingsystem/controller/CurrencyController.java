package uz.sardor.simplebankingsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.sardor.simplebankingsystem.dto.currency.request.CurrencyFilterRequest;
import uz.sardor.simplebankingsystem.dto.currency.request.CurrencyUpdateRequest;
import uz.sardor.simplebankingsystem.dto.currency.response.CurrencyResponse;
import uz.sardor.simplebankingsystem.service.CurrencyService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    @PostMapping("/update/{id}")
    public ResponseEntity<CurrencyResponse> updateCurrency(
            @RequestBody CurrencyUpdateRequest request,
            @PathVariable Long id
    ) {
        CurrencyResponse update = currencyService.update(id, request);
        return ResponseEntity.ok(update);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CurrencyResponse>> getFilteredCurrencies(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String currencyCode,
            @RequestParam(required = false) String nameUz,
            @RequestParam(required = false) BigDecimal rateFrom,
            @RequestParam(required = false) BigDecimal rateTo,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        List<CurrencyResponse> currencies = currencyService.filter(CurrencyFilterRequest.builder()
                .code(code)
                .currencyCode(currencyCode)
                .nameUz(nameUz)
                .rateFrom(rateFrom)
                .rateTo(rateTo)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build());
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/search")
    private ResponseEntity<List<CurrencyResponse>> search(
            @RequestParam(required = false) BigDecimal rateFrom,
            @RequestParam(required = false) BigDecimal rateTo,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String currencyCode,
            @RequestParam(required = false) String bankCode
    ) {
        return ResponseEntity.ok(currencyService.search(rateFrom, rateTo, code, currencyCode, bankCode));

    }
}
