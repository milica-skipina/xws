package orders.ordersmicroservice.controller;

import com.netflix.ribbon.proxy.annotation.Http;
import orders.ordersmicroservice.config.TLSConfiguration;
import orders.ordersmicroservice.config.TokenUtils;
import orders.ordersmicroservice.dto.ReportDTO;
import orders.ordersmicroservice.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/report")
public class ReportController {


    @Autowired
    private ReportService reportService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

  //  @PreAuthorize("hasAuthority('CREATE_REPORT')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value= "/{id}")
    public ResponseEntity<HttpStatus> addReport(@RequestBody ReportDTO report, HttpServletRequest request, @PathVariable Long id) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        ReportDTO reportDTO = reportService.createReport(report, id);
        if(reportDTO == null) {
            // nije prosla validacija
            logger.error("user " + username + " tried to write report");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            final String url = TLSConfiguration.URL + "advertisement/car/changeMileage/{id}";
            Map<String, Long> params = new HashMap<String, Long>();
            params.put("id", id);
            HttpEntity<Double>sentTemp = new HttpEntity<Double>(report.getMileage());
            HttpEntity<Boolean> result = restTemplate.exchange(url, HttpMethod.PUT, sentTemp, Boolean.class, params);
            logger.info("|ADD REPORT|user " + username + ", report id: " + reportDTO.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
