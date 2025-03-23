package ma.youcode.api.controllers;


import lombok.RequiredArgsConstructor;
import ma.youcode.api.payloads.responses.statisctics.StatisticsResponse;
import ma.youcode.api.services.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starter.utilities.dtos.SimpleSuccessDTO;

import static org.starter.utilities.response.Response.simpleSuccess;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statics")
    public ResponseEntity<SimpleSuccessDTO> getDashboardStatics(){

        StatisticsResponse statistics = dashboardService.loadStatistics();
        return simpleSuccess(HttpStatus.OK.value(), "Dashboard statics fetched successfully." , statistics);
    }

}
