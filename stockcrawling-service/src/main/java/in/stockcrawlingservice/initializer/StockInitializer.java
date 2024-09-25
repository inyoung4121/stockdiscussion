package in.stockcrawlingservice.initializer;


import in.stockcrawlingservice.kafka.SymbolService;
import in.stockcrawlingservice.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StockInitializer {

    private final CrawlingService crawlingService;
    private final SymbolService symbolService;

    @Bean
    CommandLineRunner initializeStockData() {
        return args -> {
            CompletableFuture.runAsync(() -> {
                try {
                    log.info("주식 데이터 초기화 시작");

                    List<String> symbols = symbolService.requestSymbols().get().getSymbols();

                    if (symbols.isEmpty()) {
                        log.error("심볼이 없어요");
                        return;
                    }

                    // 일별 데이터 처리
                    crawlingService.processDataForSymbols(symbols, crawlingService::processDailyData);

                    // 분 단위 데이터 처리
                    crawlingService.processDataForSymbols(symbols, crawlingService::processMinuteData);

                    log.info("초기화 데이터 전부 보냈음");
                } catch (Exception e) {
                    log.error("초기화 중 에러 발생: ", e);
                }
            });
        };
    }
}