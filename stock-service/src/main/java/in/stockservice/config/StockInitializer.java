package in.stockservice.config;

import in.stockservice.entity.KoreanStockMarket;
import in.stockservice.entity.MarketCapCategory;
import in.stockservice.entity.Stock;
import in.stockservice.entity.StockProductType;
import in.stockservice.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class StockInitializer {

    @Bean
    CommandLineRunner initDatabase(StockRepository repository) {
        return args -> {
            List<Stock> stocks = Arrays.asList(
                    Stock.builder()
                            .symbol("005930")
                            .name("삼성전자")
                            .chosung("ㅅㅅㅈㅈ")
                            .market(KoreanStockMarket.KOSPI)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("035720")
                            .name("카카오")
                            .chosung("ㅋㅋㅇ")
                            .market(KoreanStockMarket.KOSPI)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("005380")
                            .name("현대차")
                            .chosung("ㅎㄷㅊ")
                            .market(KoreanStockMarket.KOSPI)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("373220")
                            .name("LG에너지솔루션")
                            .chosung("LGㅇㄴㅈㅅㄹㅅ")
                            .market(KoreanStockMarket.KOSPI)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("000660")
                            .name("SK하이닉스")
                            .chosung("SKㅎㅇㄴㅅ")
                            .market(KoreanStockMarket.KOSPI)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("247540")
                            .name("에코프로비엠")
                            .chosung("ㅇㅋㅍㄹㅂㅇ")
                            .market(KoreanStockMarket.KOSDAQ)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("091990")
                            .name("셀트리온헬스케어")
                            .chosung("ㅅㅌㄹㅇㅎㅅㅋㅇ")
                            .market(KoreanStockMarket.KOSDAQ)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("028300")
                            .name("HLB")
                            .chosung("HLB")
                            .market(KoreanStockMarket.KOSDAQ)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("293490")
                            .name("카카오게임즈")
                            .chosung("ㅋㅋㅇㄱㅇㅈ")
                            .market(KoreanStockMarket.KOSDAQ)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build(),
                    Stock.builder()
                            .symbol("086520")
                            .name("에코프로")
                            .chosung("ㅇㅋㅍㄹ")
                            .market(KoreanStockMarket.KOSDAQ)
                            .productType(StockProductType.STOCK)
                            .marketCapCategory(MarketCapCategory.LARGE_CAP)
                            .build()
            );

            for (Stock stock : stocks) {
                repository.findBySymbol(stock.getSymbol())
                        .ifPresentOrElse(
                                existingStock -> {
                                    // 기존 데이터가 있다면 업데이트
                                    existingStock.setName(stock.getName());
                                    existingStock.setChosung(stock.getChosung());
                                    existingStock.setMarket(stock.getMarket());
                                    existingStock.setProductType(stock.getProductType());
                                    existingStock.setMarketCapCategory(stock.getMarketCapCategory());
                                    repository.save(existingStock);
                                },
                                () -> {
                                    // 기존 데이터가 없다면 새로 저장
                                    repository.save(stock);
                                }
                        );
            }
        };
    }

    private String getChosung(String syllable) {
        String[] chosungList = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
        char ch = syllable.charAt(0);
        if (ch >= '가' && ch <= '힣') {
            int index = (ch - '가') / 588;
            return chosungList[index];
        }
        return syllable;
    }
}
