package in.stockcrawlingservice.config;

import in.stockcrawlingservice.SymbolCacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AppConfig {
    private final SymbolCacheService symbolCacheService;

    @PostConstruct
    public void init() {
        symbolCacheService.initializeSymbols();
    }
}