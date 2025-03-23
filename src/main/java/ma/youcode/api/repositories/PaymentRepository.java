package ma.youcode.api.repositories;

import ma.youcode.api.models.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.starter.utilities.repositories.GenericRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends GenericRepository<Payment, UUID> {

    @Query("SELECT SUM(p.amount) FROM Payment p")
    double countTotalAmounts();


    @Query(value = """
            SELECT TO_CHAR(d.full_date, 'FMDay') AS day,
                COALESCE(SUM(p.amount), 0) AS value
            FROM (
                SELECT generate_series(
                    date_trunc('week', CURRENT_DATE), 
                    CURRENT_DATE, 
                    INTERVAL '1 day'
                ) AS full_date
            ) d
                LEFT JOIN payments p 
                ON TO_CHAR(p.created_at, 'YYYY-MM-DD') = TO_CHAR(d.full_date, 'YYYY-MM-DD')
                AND p.payment_status = 'SUCCEEDED'
            GROUP BY d.full_date
            ORDER BY d.full_date
            """, nativeQuery = true)
    List<Object[]> calculateWeeklyIncome();


    @Query(value = """
            SELECT TO_CHAR(d.date, 'FMMonth') AS name,
                COALESCE(SUM(p.amount), 0) AS value
            FROM (
                SELECT generate_series(
                    date_trunc('year', CURRENT_DATE), 
                    CURRENT_DATE, 
                    INTERVAL '1 month'
                ) AS date
            ) d
                LEFT JOIN payments p 
                ON TO_CHAR(p.created_at, 'YYYY-MM') = TO_CHAR(d.date, 'YYYY-MM')
                AND p.payment_status = 'SUCCEEDED'
            GROUP BY d.date
            ORDER BY d.date
            """, nativeQuery = true)
    List<Object[]> calculateMonthlyIncome();


}
