package in.stockservice.Entity;

public enum KoreanStockMarket {
    KOSPI("코스피"),
    KOSDAQ("코스닥"),
    KONEX("코넥스"),
    K_OTC("K-OTC"),
    K_OTCBB("K-OTCBB"),
    OTHER("기타");

    private final String name;

    KoreanStockMarket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
