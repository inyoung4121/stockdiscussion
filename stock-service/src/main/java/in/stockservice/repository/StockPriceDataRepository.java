package in.stockservice.repository;

import in.stockservice.entity.StockPriceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockPriceDataRepository extends JpaRepository<StockPriceData, Integer> {
}
