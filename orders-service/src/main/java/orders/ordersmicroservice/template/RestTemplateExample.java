package orders.ordersmicroservice.template;

import orders.ordersmicroservice.config.TLSConfiguration;
import orders.ordersmicroservice.config.TokenUtils;
import orders.ordersmicroservice.dto.BasketDTO;
import orders.ordersmicroservice.dto.CarOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RestTemplateExample {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    public Hashtable<Long, BasketDTO> postSender(String url, ArrayList<Long> data, String jwt) {
        Hashtable<Long, BasketDTO> exchange = new Hashtable<>();
        HttpEntity<ArrayList<Long>> bodyData = createAuthHeader(jwt, data);
        ResponseEntity<BasketDTO[]> responseEntityStr = restTemplate.postForEntity(url, bodyData, BasketDTO[].class);
        if (responseEntityStr.getStatusCode().is2xxSuccessful()) {
            for(int i=0; i < responseEntityStr.getBody().length; i++) {
                if (!exchange.contains(responseEntityStr.getBody()[i].getAdvertisementId())) {
                    exchange.put(responseEntityStr.getBody()[i].getAdvertisementId(), responseEntityStr.getBody()[i]);
                }
            }
        }
        return exchange;
    }

    public CarOrderDTO getCar(String url, String jwt) {
        HttpEntity header = createAuthHeader(jwt, null);
        ResponseEntity<CarOrderDTO> car = restTemplate.exchange(url, HttpMethod.GET, header, CarOrderDTO.class);
        return car.getBody();
    }

    public void postCreateUser(String url, String[] data, String jwt) {
        ArrayList<String> data1 = new ArrayList<String>();
        data1.addAll(Arrays.asList(data));
        HttpEntity<ArrayList<String>> bodyData = createAuthHeader(jwt, data1);
        ResponseEntity<Boolean> responseEntityStr = restTemplate.postForEntity(url, bodyData, boolean.class);
    }

    private <T> HttpEntity<T> createAuthHeader(String token, T bodyType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<T> request = new HttpEntity<>(bodyType, headers);
        return request;
    }

    public void changeNumber(String username, String jwt){
        final String url = TLSConfiguration.URL + "authpoint/user/changeRefusedNumber/{username}";
        HttpEntity header = createAuthHeader(jwt, null);
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        HttpEntity<Integer>sentTemp = new HttpEntity<Integer>(1);
        HttpEntity<Boolean> result = restTemplate.exchange(url, HttpMethod.PUT, sentTemp, Boolean.class, params);
    }
}
