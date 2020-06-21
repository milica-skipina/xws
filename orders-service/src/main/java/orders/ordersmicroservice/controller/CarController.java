package orders.ordersmicroservice.controller;

import java.util.List;
import com.netflix.ribbon.proxy.annotation.Http;
import orders.ordersmicroservice.config.TLSConfiguration;
import orders.ordersmicroservice.config.TokenUtils;
import orders.ordersmicroservice.dto.CarOrderDTO;
import orders.ordersmicroservice.dto.ReportDTO;
import orders.ordersmicroservice.service.CarService;
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

@RestController
@RequestMapping(value = "/car")
public class CarController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private CarService carService;

    @PreAuthorize("hasAuthority('READ_STATISTICS')")
    @GetMapping(produces="application/json", value="/statistics")
    public ResponseEntity<List<CarOrderDTO>> getStatistics(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        List<CarOrderDTO> cars = carService.getStatistics(username);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
}
