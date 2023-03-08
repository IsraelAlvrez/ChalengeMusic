package com.az.challengemusic.servicios;

import com.az.challengemusic.controladores.ControladorTemperatura;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServicioClima {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicioClima.class);
  RestTemplate restTemplate= new RestTemplate();
  @Value("${clima.api_key}")
  String apikey;

  @Value("${clima.Url_base}")
  String urlBase;


    public Double ObtenerTemperaturaPorNombre(String ciudad) throws JsonProcessingException {
        LOGGER.debug("ObtenerTemperaturaPorNombre");

        ResponseEntity<String> response = restTemplate.getForEntity(urlBase +"q="+ ciudad+"&appid="+apikey, String.class);
        LOGGER.debug(response.getBody());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        double temp = root.path("main").path("temp").doubleValue();
        LOGGER.debug(String.valueOf(temp));
        double temperatura = temp-273.15;
        return temperatura;
    }
    public Double ObtenerTemperaturaPorLonyLat(Double lon,Double lat) throws JsonProcessingException {
        ResponseEntity<String>response = restTemplate.getForEntity(urlBase +"lat="+lat+ "&lon="+lon+"&appid="+apikey, String.class);
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            double temp = root.path("main").path("temp").doubleValue();
            double temperatura = temp-273.15;
            return temperatura;
        }
}

}

