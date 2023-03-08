package com.az.challengemusic.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;


@Service
public class ServicioPrincipal {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicioPrincipal.class);

    ServicioClima ServClima;
    ServicioSpotify serviSpotify ;




    public ServicioPrincipal(ServicioClima servClima, ServicioSpotify serviSpotify) {
        ServClima = servClima;
        this.serviSpotify = serviSpotify;
    }

    public List<String> obtenerListaCancionesPorNombre(String ciudad) throws JsonProcessingException {
        Double temperatura =  ServClima.ObtenerTemperaturaPorNombre(ciudad);
        String genero = ObtenerGeneroDeMusica(temperatura);
        return serviSpotify.listaDeCanciones(genero);
    }

    public List<String> ObtenerTemperaturaPorLonyLat(Double lon,Double lat) throws JsonProcessingException {
        Double temperatura = ServClima.ObtenerTemperaturaPorLonyLat(lon,lat);
        String genero = ObtenerGeneroDeMusica(temperatura);
        return serviSpotify.listaDeCanciones(genero);
    }


    public String ObtenerGeneroDeMusica(Double temp){
        String generoMusica ;

        if (temp> 30 ){
            generoMusica="party";}
        else if (temp > 15 || temp <=30 ){
            generoMusica = "pop";

        }else if (temp >10 || temp <=15 ){
            generoMusica="rock";

        }else {
            generoMusica= "classic";
        }

        return generoMusica;
    }





}

