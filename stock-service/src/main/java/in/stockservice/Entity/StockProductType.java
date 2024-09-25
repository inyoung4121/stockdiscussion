package in.stockservice.entity;

public enum StockProductType {
    STOCK("주식"),
    ETF("상장지수펀드"),
    ELW("주식워런트증권"),
    ETN("상장지수증권"),
    REIT("부동산투자신탁"),
    OTHER("기타");

    private final String name;

    StockProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
