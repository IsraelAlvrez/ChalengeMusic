package com.az.challengemusic.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static java.util.Collections.EMPTY_MAP;

@Service
public class ServicioSpotify {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicioSpotify.class);

    @Value("${spotify.client_secret}")
    String clientSecret;

    @Value("${spotify.client_id}")
    String clientId;
    private String SolicitarAutorizacion() throws JsonProcessingException {
        LOGGER.debug("SolicitarAutorizacion");
        String cadena= clientId +":"+clientSecret;
        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(cadena.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic "+encoded);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate= new RestTemplate();
        String CreacionDeToken  =restTemplate.exchange("https://accounts.spotify.com/api/token", HttpMethod.POST,request, String.class).getBody();

        LOGGER.debug(CreacionDeToken);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(CreacionDeToken);
        String token = root.path("access_token").asText();

        return  token;
    }
    public List<String>listaDeCanciones (String genero) throws JsonProcessingException {
        List<String>ListaDeNombres = new ArrayList<>();
        String token = SolicitarAutorizacion();

        UriComponents uriComponents= UriComponentsBuilder.fromHttpUrl("https://api.spotify.com/v1/search?")
                .queryParam("q","genre:"+genero)
                .queryParam("type","track")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,"Bearer "+token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity request = new HttpEntity<>(EMPTY_MAP, headers);
        RestTemplate restTemplate = new RestTemplate();
        String resultado = restTemplate.exchange(uriComponents.toUriString(),HttpMethod.GET,request,String.class).getBody();


        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(resultado);
        JsonNode listaDeitems = root.path("tracks").path("items");
        if (listaDeitems.isArray()) {
            for (final JsonNode objNode : listaDeitems) {
                ListaDeNombres.add(objNode.path("name").asText());
            }
        }





        return ListaDeNombres;
    }



}
