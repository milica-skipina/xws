package orders.ordersmicroservice.template;

import orders.ordersmicroservice.dto.BasketDTO;
import orders.ordersmicroservice.dto.CarOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Service
public class RestTemplateExample {

    @Autowired
    private RestTemplate restTemplate;

    public Hashtable<Long, BasketDTO> postSender(String url, ArrayList<Long> data) {
        Hashtable<Long, BasketDTO> exchange = new Hashtable<>();
        ResponseEntity<BasketDTO[]> responseEntityStr = restTemplate.postForEntity(url, data, BasketDTO[].class);
        if (responseEntityStr.getStatusCode().is2xxSuccessful()) {
            for(int i=0; i < responseEntityStr.getBody().length; i++) {
                if (!exchange.contains(responseEntityStr.getBody()[i].getAdvertisementId())) {
                    exchange.put(responseEntityStr.getBody()[i].getAdvertisementId(), responseEntityStr.getBody()[i]);
                }
            }
        }
        return exchange;
    }

    public CarOrderDTO getCar(String url, Long id) {
        ResponseEntity<CarOrderDTO> car = restTemplate.getForEntity(url, CarOrderDTO.class);
        return car.getBody();
    }
}
