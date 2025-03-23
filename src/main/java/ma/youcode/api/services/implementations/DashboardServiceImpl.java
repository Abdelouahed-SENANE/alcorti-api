package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.RoleType;
import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.payloads.responses.statisctics.ChartItem;
import ma.youcode.api.payloads.responses.statisctics.IncomeResponse;
import ma.youcode.api.payloads.responses.statisctics.MetricsOverview;
import ma.youcode.api.payloads.responses.statisctics.StatisticsResponse;
import ma.youcode.api.repositories.PaymentRepository;
import ma.youcode.api.repositories.ShipmentRepository;
import ma.youcode.api.repositories.UserRepository;
import ma.youcode.api.services.DashboardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {


    private static final Logger log = LogManager.getLogger(DashboardServiceImpl.class);
    private final UserRepository userRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;


    @Override
    public StatisticsResponse loadStatistics() {

        return StatisticsResponse.builder()
                .usersMetrics(usersAnalytics())
                .activeUsersMetrics(activeUsersAnalytics())
                .customersMetrics(customersAnalytics())
                .driversMetrics(driversAnalytics())
                .shipmentsMetrics(shipmentAnalytics())
                .income(getIncomeOverview())
                .countShipmentByStatus(countAndGroupShipmentByStatus())
                .build();
    }

    private IncomeResponse getIncomeOverview() {

        List <Object[]>  weeklyIncome = paymentRepository.calculateWeeklyIncome();
        List <Object[]> monthlyIncome = paymentRepository.calculateMonthlyIncome();

        return IncomeResponse.builder()
                .weekly(toChartItems(weeklyIncome))
                .monthly(toChartItems(monthlyIncome))
                .build();
    }

    private List<ChartItem> toChartItems(List<Object[]> list) {

        return list.stream()
                .map(o -> new ChartItem(o[0].toString(), Double.parseDouble(o[1].toString())))
                .toList();

    }

    private  List<ChartItem> countAndGroupShipmentByStatus() {
        List<Object[]> countShipmentByShipmentStatus = shipmentRepository.countShipmentsGroupedByStatus();
        Map<ShipmentStatus, Long> statusCounts = new EnumMap<>(ShipmentStatus.class);
        for (ShipmentStatus status : ShipmentStatus.values()) {
            statusCounts.put(status, 0L);
        }

        for (Object[] row : countShipmentByShipmentStatus) {
            ShipmentStatus status = (ShipmentStatus) row[0];
            Long count = (Long) row[1];
            statusCounts.put(status, count);
        }

        return statusCounts.entrySet().stream()
                .map(entry -> new ChartItem(entry.getKey().name(), entry.getValue()))
                .toList();
    }




    private MetricsOverview activeUsersAnalytics() {

        long totalActiveUsers = userRepository.countActiveUsersSince(getStartOfCurrentWeek());

        long totalUsers = userRepository.count();
        double activeUsersPercentage = calculatePercentage(totalUsers , totalActiveUsers);

        return  buildMetrics(totalActiveUsers , totalUsers , activeUsersPercentage);
    }

    private MetricsOverview usersAnalytics() {

        long totalUsers = userRepository.count();

        long registeredUsersInThisWeek = userRepository.countByCreatedAtBetween(
                getStartOfCurrentWeek(),
                getEndOfCurrentWeek());

        double usersPercentage = calculatePercentage(totalUsers , registeredUsersInThisWeek);
        return  buildMetrics(totalUsers , registeredUsersInThisWeek , usersPercentage);

    }

    private LocalDateTime getStartOfCurrentWeek() {
        return LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfWeek().getValue() - 1);
    }

    private LocalDateTime getEndOfCurrentWeek() {
        return getStartOfCurrentWeek().plusDays(6);
    }

    private LocalDateTime getStartOfLastWeek() {
        return getStartOfCurrentWeek().minusWeeks(1);
    }

    private MetricsOverview customersAnalytics() {
        long totalCustomers = userRepository.countByRole(RoleType.ROLE_CUSTOMER);

        long registeredCustomersInThisWeek = userRepository.countByRoleAndCreatedAtBetween(RoleType.ROLE_CUSTOMER,
                getStartOfCurrentWeek(),
                getEndOfCurrentWeek());

        double customersPercentage = calculatePercentage(totalCustomers , registeredCustomersInThisWeek);
        return buildMetrics(totalCustomers , registeredCustomersInThisWeek , customersPercentage);
    }

    private MetricsOverview driversAnalytics() {
        long totalDrivers = userRepository.countByRole(RoleType.ROLE_DRIVER);
        long registeredDriversInThisWeek = userRepository.countByRoleAndCreatedAtBetween(RoleType.ROLE_CUSTOMER, getStartOfCurrentWeek() , getEndOfCurrentWeek());
        double driversPercentage = calculatePercentage(totalDrivers , registeredDriversInThisWeek);
        return buildMetrics(totalDrivers, registeredDriversInThisWeek , driversPercentage);

    }

    private MetricsOverview buildMetrics(long total, long previousTotal , double percentage) {
        return MetricsOverview.builder()
                .total(total)
                .previousTotal(previousTotal)
                .percentage(percentage)
                .build();
    }

    private Pair<LocalDateTime, LocalDateTime> getPreviousWeekRange() {
        LocalDateTime startOfPreviousWeek = LocalDateTime.now()
                .minusWeeks(1)
                .with(DayOfWeek.MONDAY)
                .truncatedTo(ChronoUnit.DAYS);

        LocalDateTime endOfPreviousWeek = LocalDateTime.now()
                .minusWeeks(1)
                .with(DayOfWeek.SUNDAY)
                .with(LocalTime.MAX);
        return Pair.of(startOfPreviousWeek, endOfPreviousWeek);
    }

    private MetricsOverview shipmentAnalytics() {
        long totalShipments = shipmentRepository.count();
        long totalShipmentInLastMonth = shipmentRepository.countByCreatedAtBetween(getStartOfCurrentWeek(),getEndOfCurrentWeek());
        double percentage = calculatePercentage(totalShipments, totalShipmentInLastMonth);
        return buildMetrics(totalShipments, totalShipmentInLastMonth , percentage);
    }

    private double calculatePercentage(long total, long current) {
        if (total == 0) return current > 0 ? 100.0 : 0.0;
        return ((double) (current ) / total) * 100;
    }

    private List<Object[]> calculateWeeklyIncome() { return null; }
}
