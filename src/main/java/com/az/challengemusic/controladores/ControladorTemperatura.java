package com.az.challengemusic.controladores;

import com.az.challengemusic.servicios.ServicioPrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@RestController
public class ControladorTemperatura {

    ServicioPrincipal ServiPrincipal;
    private static final Logger LOGGER = LoggerFactory.getLogger(ControladorTemperatura.class);

    public ControladorTemperatura(ServicioPrincipal serviPrincipal) {
        ServiPrincipal = serviPrincipal;
    }

    @GetMapping(value = "ListaCanciones",params = "ciudad")
    public  List<String>ListaCanciones(@RequestParam("ciudad") String ciudad) throws JsonProcessingException {
        LOGGER.debug("GET ListaCanciones");
       return ServiPrincipal.obtenerListaCancionesPorNombre(ciudad);
    }

    @GetMapping(value = "ListaCanciones",params ={"lat","log"} )
    public List<String>ListaCanciones(@RequestParam("lat")Double lat,
                                        @RequestParam("log")Double lon) throws JsonProcessingException {
        LOGGER.debug("GET ListaCanciones");
        return ServiPrincipal.ObtenerTemperaturaPorLonyLat(lon,lat);
    }
}
