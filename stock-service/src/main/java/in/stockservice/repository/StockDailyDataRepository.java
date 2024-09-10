package in.stockservice.repository;

import in.stockservice.Entity.StockDailyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDailyDataRepository extends JpaRepository<StockDailyData, Long> {
}
