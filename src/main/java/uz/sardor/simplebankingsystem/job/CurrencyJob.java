package uz.sardor.simplebankingsystem.job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.sardor.simplebankingsystem.dto.currency.CurrencyDTO;
import uz.sardor.simplebankingsystem.entity.Currency;
import uz.sardor.simplebankingsystem.exception.exception.ResourceNotFoundException;
import uz.sardor.simplebankingsystem.repository.CurrencyRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CurrencyJob {

    private final RestTemplate restTemplate;
    private final CurrencyRepository currencyRepository; // Ma'lumotlarni saqlash uchun repository

    @Autowired
    public CurrencyJob(RestTemplate restTemplate, CurrencyRepository currencyRepository) {
        this.restTemplate = restTemplate;
        this.currencyRepository = currencyRepository;
    }
    @Value("${cbu.api}")
    private String CBU_API;
    /**
     * Har kuni 12:00 da valyuta kurslarini olib kelib, bazaga yozib qo'yadi.
     * Cron ifodasi: "0 0 12 * * *" => 0 sekundda, 0 minutda, 12-soatda, har kuni, har oyda.
     */
    @Scheduled(cron = "${app.currency.schedule}")
    public void fetchAndSaveCurrencyRates() {

        CurrencyDTO[] currencyRates = restTemplate.getForObject(CBU_API, CurrencyDTO[].class);
        if (currencyRates != null) {
            for (CurrencyDTO rateDto : currencyRates) {
                Currency entity = toEntity(rateDto);
                currencyRepository.save(entity);
            }
        } else {
        throw new ResourceNotFoundException("Currency rates not found");
        }
    }

    private Currency toEntity(CurrencyDTO currencyDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(currencyDto.getDate(), formatter);
        return Currency.builder()
                .code(currencyDto.getCode())
                .currencyCode(currencyDto.getCcy())
                .nameUz(currencyDto.getCcyNmUz())
                .nameUzc(currencyDto.getCcyNmUzc())
                .nameEng(currencyDto.getCcyNmEn())
                .nameRu(currencyDto.getCcyNmRu())
                .rate(currencyDto.getRate())
                .nominal(currencyDto.getNominal())
                .difference(currencyDto.getDiff())
                .date(date)
                .build();
    }
}