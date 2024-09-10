package in.stockcrawlingservice.initializer;


import in.stockcrawlingservice.dto.dailydatadto.StockDailyDataDTO;
import in.stockcrawlingservice.dto.dailydatadto.StockDailyDataListDTO;
import in.stockcrawlingservice.kafka.StockDailyDataService;
import in.stockcrawlingservice.kafka.SymbolService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class StockInitializer {

    private final SymbolService symbolService;
    private final RestTemplate restTemplate;
    private final StockDailyDataService stockDailyDataService;

    @Bean
    CommandLineRunner initailizeStockDailyData() {
        return args -> {
            symbolService.requestSymbols()
                    .thenApply(response -> {
                        List<String> symbols = response.getSymbols();

                        symbols.forEach(symbol -> {
                            String url = "https://fchart.stock.naver.com/sise.nhn?symbol=" + symbol + "&timeframe=day&count=1303&requestType=0";
                            String rawData = fetchDataFromUrl(url);
                            stockDailyDataService.sendStockData(parseStockData(rawData,symbol));

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                        });
                        return response;
                    })
                    .exceptionally(ex -> {

                        System.err.println("Error fetching symbols: " + ex.getMessage());
                        return null;
                    });
        };
    }

    public StockDailyDataListDTO parseStockData(String rawData, String symbol) {
        StockDailyDataListDTO stockDailyDataListDTO = new StockDailyDataListDTO();
        stockDailyDataListDTO.setSymbol(symbol);
        List<StockDailyDataDTO> result = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(rawData)));
            NodeList itemList = doc.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                StockDailyDataDTO data = new StockDailyDataDTO();

                data.setSymbol(symbol);
                data.setDate(LocalDate.parse(item.getAttribute("data"), formatter));
                data.setOpenPrice(new BigDecimal(item.getAttribute("start")));
                data.setHighPrice(new BigDecimal(item.getAttribute("high")));
                data.setLowPrice(new BigDecimal(item.getAttribute("low")));
                data.setClosePrice(new BigDecimal(item.getAttribute("end")));
                data.setVolume(Long.parseLong(item.getAttribute("volume")));

                result.add(data);
            }
        } catch (Exception e) {
            // 로깅 및 예외 처리
            e.printStackTrace();
        }
        stockDailyDataListDTO.setDataList(result);
        return stockDailyDataListDTO;
    }

    public String fetchDataFromUrl(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
