package in.stockcrawlingservice;

import in.stockcrawlingservice.kafka.SymbolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SymbolCacheService {
    private final SymbolService symbolService;
    private List<String> cachedSymbols = new ArrayList<>();

    public SymbolCacheService(SymbolService symbolService) {
        this.symbolService = symbolService;
    }

    public void initializeSymbols() {
        CompletableFuture.runAsync(() -> {
            try {
                List<String> symbols = symbolService.requestSymbols().get().getSymbols();
                cachedSymbols.addAll(symbols);
                log.info("주식코드 캐싱 완료: {}", cachedSymbols);
            } catch (Exception e) {
                log.error("주식코드 캐싱 중 에러 발생: ", e);
            }
        });
    }

    public List<String> getCachedSymbols() {
        return new ArrayList<>(cachedSymbols);
    }
}