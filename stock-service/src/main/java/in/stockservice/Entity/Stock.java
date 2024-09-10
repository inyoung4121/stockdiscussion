package in.stockservice.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String symbol;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    private String chosung;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KoreanStockMarket market;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockProductType productType;

    @Enumerated(EnumType.STRING)
    private MarketCapCategory marketCapCategory;
}
