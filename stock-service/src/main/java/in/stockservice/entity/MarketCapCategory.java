package in.stockservice.entity;

public enum MarketCapCategory {
    LARGE_CAP("대형주"),
    MID_CAP("중형주"),
    SMALL_CAP("소형주");

    private final String name;

    MarketCapCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
