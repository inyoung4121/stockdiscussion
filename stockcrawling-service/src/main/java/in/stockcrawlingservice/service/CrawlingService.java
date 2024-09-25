package in.stockcrawlingservice.service;

import in.stockcrawlingservice.DateRangeFormatter;
import in.stockcrawlingservice.dto.dailydatadto.StockPriceDataDTO;
import in.stockcrawlingservice.dto.dailydatadto.StockPriceDataListDTO;
import in.stockcrawlingservice.kafka.StockDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlingService {
    private final RestTemplate restTemplate;
    private final StockDataService stockDataService;

    public void processDataForSymbols(List<String> symbols, SymbolDataProcessor processor) {
        for (String symbol : symbols) {
            try {
                processor.process(symbol);
                sleep();
            } catch (Exception e) {
                log.error("해당 주식코드 처리 중 에러 발생 {}: {}", symbol, e.getMessage(), e);
            }
        }
    }

    public void processDailyData(String symbol) throws Exception {
        String url = "https://fchart.stock.naver.com/sise.nhn?symbol=" + symbol + "&timeframe=day&count=1303&requestType=0";
        String rawData = fetchDataFromUrl(url);
        StockPriceDataListDTO stockDataList = parseXMLData(rawData, symbol);
        stockDataService.sendStockData(stockDataList);
        log.info("해당 코드 일별 데이터 전송: {}", symbol);
    }

    public void processMinuteData(String symbol) throws Exception {
        String[] dateRange = DateRangeFormatter.getDateRange(5);
        String startDateTime = dateRange[0];
        String endDateTime = dateRange[1];
        String url = "https://api.stock.naver.com/chart/domestic/item/" +
                symbol + "/minute?startDateTime=" + startDateTime + "&endDateTime=" + endDateTime;
        String rawData = fetchDataFromUrl(url);
        StockPriceDataListDTO stockDataList = parseJSONData(rawData, symbol);
        stockDataService.sendStockData(stockDataList);
        log.info("해당 코드 분 단위 데이터 전송: {}", symbol);
    }

    public StockPriceDataListDTO parseXMLData(String xmlString, String symbol) {
        StockPriceDataListDTO stockDataList = new StockPriceDataListDTO();
        stockDataList.setSymbol(symbol);
        stockDataList.setDataType("DAILY");
        List<StockPriceDataDTO> dataList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            Document doc = builder.parse(is);

            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                String data = item.getAttribute("data");
                StockPriceDataDTO stockData = convertToStockPriceDataDTO(data, symbol, "DAILY");
                dataList.add(stockData);
            }
        } catch (Exception e) {
            log.error("일별 데이터 파싱 중 에러 {}: {}", symbol, e.getMessage(), e);
        }

        stockDataList.setDataList(dataList);
        return stockDataList;
    }

    public StockPriceDataListDTO parseJSONData(String jsonString, String symbol) {
        StockPriceDataListDTO stockDataList = new StockPriceDataListDTO();
        stockDataList.setSymbol(symbol);
        stockDataList.setDataType("MINUTE");
        List<StockPriceDataDTO> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject priceObj = jsonArray.getJSONObject(i);
                StockPriceDataDTO stockData = convertToStockPriceDataDTO(priceObj, symbol, "MINUTE");
                dataList.add(stockData);
            }
        } catch (Exception e) {
            log.error("분단위 데이터 파싱 중 에러 {}: {}", symbol, e.getMessage(), e);
        }

        stockDataList.setDataList(dataList);
        return stockDataList;
    }

    private StockPriceDataDTO convertToStockPriceDataDTO(String data, String symbol, String dataType) {
        String[] parts = data.split("\\|");
        StockPriceDataDTO dto = new StockPriceDataDTO();
        dto.setSymbol(symbol);
        dto.setDataType(dataType);
        dto.setDatetime(LocalDateTime.parse(parts[0] + "1530", DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
        dto.setOpenPrice(new BigDecimal(parts[1]));
        dto.setHighPrice(new BigDecimal(parts[2]));
        dto.setLowPrice(new BigDecimal(parts[3]));
        dto.setClosePrice(new BigDecimal(parts[4]));
        dto.setVolume(Long.parseLong(parts[5]));
        return dto;
    }

    private StockPriceDataDTO convertToStockPriceDataDTO(JSONObject priceObj, String symbol, String dataType) {
        StockPriceDataDTO dto = new StockPriceDataDTO();
        dto.setSymbol(symbol);
        dto.setDataType(dataType);
        dto.setDatetime(LocalDateTime.parse(priceObj.getString("localDateTime"), DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        dto.setOpenPrice(new BigDecimal(priceObj.getDouble("openPrice")));
        dto.setHighPrice(new BigDecimal(priceObj.getDouble("highPrice")));
        dto.setLowPrice(new BigDecimal(priceObj.getDouble("lowPrice")));
        dto.setClosePrice(new BigDecimal(priceObj.getDouble("currentPrice")));
        dto.setVolume(priceObj.getLong("accumulatedTradingVolume"));
        return dto;
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("대기 중 에러: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public String fetchDataFromUrl(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    @FunctionalInterface
    public interface SymbolDataProcessor {
        void process(String symbol) throws Exception;
    }
}
