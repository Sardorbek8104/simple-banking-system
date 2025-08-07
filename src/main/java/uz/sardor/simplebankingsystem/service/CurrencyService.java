package uz.sardor.simplebankingsystem.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.sardor.simplebankingsystem.dto.currency.request.CurrencyFilterRequest;
import uz.sardor.simplebankingsystem.dto.currency.request.CurrencyUpdateRequest;
import uz.sardor.simplebankingsystem.dto.currency.response.CurrencyResponse;
import uz.sardor.simplebankingsystem.entity.Currency;
import uz.sardor.simplebankingsystem.exception.exception.ResourceNotFoundException;
import uz.sardor.simplebankingsystem.mapper.CurrencyMapper;
import uz.sardor.simplebankingsystem.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final RestTemplate restTemplate;

    public CurrencyResponse update(Long id, CurrencyUpdateRequest request) {
        Optional<Currency> byId = currencyRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ResourceNotFoundException("Currency with id " + id + " not found");
        }
        Currency currency = byId.get();

        BigDecimal oldRate = currency.getRate();
        currency.setRate(request.getRate());
        BigDecimal newRate = request.getRate();
        BigDecimal difference = newRate.subtract(oldRate);

        currency.setDifference(difference);

        Currency save = currencyRepository.save(currency);

        return currencyMapper.toCurrencyResponse(save);
    }

    public List<CurrencyResponse> filter(CurrencyFilterRequest filter) {
        Specification<Currency> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getCode() != null && !filter.getCode().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("code"), filter.getCode()));
            }

            if (filter.getCurrencyCode() != null && !filter.getCurrencyCode().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("currencyCode"), filter.getCurrencyCode()));
            }

            if (filter.getNameUz() != null && !filter.getNameUz().isBlank()) {
                predicates.add(criteriaBuilder.like(root.get("nameUz"), "%" + filter.getNameUz() + "%"));
            }

            if (filter.getRateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rate"), filter.getRateFrom()));
            }

            if (filter.getRateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rate"), filter.getRateTo()));
            }

            if (filter.getDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getDateFrom()));
            }

            if (filter.getDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getDateTo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Currency> currencies = currencyRepository.findAll(specification);

        return currencies.stream()
                .map(currencyMapper::toCurrencyResponse)
                .collect(Collectors.toList());
    }


    public List<CurrencyResponse> search(BigDecimal rateFrom, BigDecimal rateTo, String code, String currencyCode, String bankCode) {
        Specification<Currency> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
CurrencyFilterRequest filter = CurrencyFilterRequest.builder()
        .rateFrom(rateFrom)
        .rateTo(rateTo)
        .code(code)
        .currencyCode(currencyCode)
        .build();
            if (filter.getCode() != null && !filter.getCode().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("code"), filter.getCode()));
            }

            if (filter.getCurrencyCode() != null && !filter.getCurrencyCode().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("currencyCode"), filter.getCurrencyCode()));
            }

            if (filter.getRateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rate"), filter.getRateFrom()));
            }

            if (filter.getRateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rate"), filter.getRateTo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Currency> currencies = currencyRepository.findAll(specification);

        return currencies.stream()
                .map(currencyMapper::toCurrencyResponse)
                .collect(Collectors.toList());
    }

}