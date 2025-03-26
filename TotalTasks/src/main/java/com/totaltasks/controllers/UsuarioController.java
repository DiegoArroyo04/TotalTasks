package com.totaltasks.controllers;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.UsuarioDTO;
import com.totaltasks.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/usuarios/")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("registrar")
    public String registrarUsuario(@RequestBody UsuarioDTO usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @PostMapping("registrarGoogle")
    public String registrarUsuarioGoogle(@RequestBody UsuarioDTO usuario, HttpSession session) {

        // Llamamos al servicio para registrar al usuario o iniciar sesión si ya existe
        String respuesta = usuarioService.registrarUsuarioGoogle(usuario);

        // Si el usuario fue encontrado (existe en la base de datos), se agrega a la
        // sesión
        if (respuesta.equals("Usuario encontrado. Iniciando sesión...")) {
            session.setAttribute("usuario", usuarioService.encontrarUsuario(usuario.getUsuario()));
        } else {
            // Si el usuario fue creado, también lo agregamos a la sesión
            session.setAttribute("usuario", usuarioService.encontrarUsuario(usuario.getUsuario()));
        }

        return respuesta;

    }

    public String obtenerAccessTokenDeGitHub(String code) {
        RestTemplate restTemplate = new RestTemplate();
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "Ov23li9EsZ9MUsqhPpoX");
        params.add("client_secret", "0b382c7410bfde696afcc987b8423cecd50fa30a"); // Reemplaza por tu Client Secret real
        params.add("code", code);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Agregamos la cabecera para recibir la respuesta en formato JSON
        headers.set("Accept", "application/json");
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        String tokenUrl = "https://github.com/login/oauth/access_token";
        
        // Solicitamos la respuesta como un Map para poder extraer el token fácilmente
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        return responseBody != null ? (String) responseBody.get("access_token") : null;
    }
    
    public UsuarioDTO obtenerDatosUsuarioGitHub(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        String userUrl = "https://api.github.com/user";
        ResponseEntity<Map> response = restTemplate.exchange(userUrl, HttpMethod.GET, entity, Map.class);
        
        Map userData = response.getBody();
        
        // Extraemos los datos relevantes del usuario
        String nombre = (String) userData.get("name");
        String usuario = (String) userData.get("login");
        String email = (String) userData.get("email");
        
        // En caso de que email venga nulo, se puede asignar un valor por defecto
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombre != null ? nombre : usuario);
        usuarioDTO.setUsuario(usuario);
        usuarioDTO.setEmail(email != null ? email : usuario + "@github.com");
        
        return usuarioDTO;
    }
    
    @GetMapping("githubCallback")
    public String githubCallback(@RequestParam("code") String code, HttpSession session) {
        // 1. Intercambiar el code por un token de acceso
        String accessToken = obtenerAccessTokenDeGitHub(code);
        
        // 2. Usar el token para obtener información del usuario
        UsuarioDTO usuarioDTO = obtenerDatosUsuarioGitHub(accessToken);
        
        // 3. Registrar o iniciar sesión según corresponda
        String respuesta = usuarioService.registrarUsuarioGitHub(usuarioDTO);
        
        // Guardar el usuario en sesión (si fuera necesario)
        if (respuesta.contains("Iniciando sesión") || respuesta.contains("Cuenta creada")) {
            session.setAttribute("usuario", usuarioService.encontrarUsuario(usuarioDTO.getUsuario()));
        }
        
        // Redirigir o devolver la respuesta
        return respuesta;
    }    

    @PostMapping("comprobarLogin")
    public String comprobarLogin(@RequestBody UsuarioDTO usuario, HttpSession session) {

        // SI ENCONTRAMOS EL USUARIO Y ESTE ES VALIDADO LO AÑADIMOS A LA SESSION QUE NO
        // DEVOLVEMOS EL ERROR
        if (usuarioService.comprobarLogin(usuario).equals("Encontrado")) {

            UsuarioEntity usuarioEntity = usuarioService.encontrarUsuario(usuario.getUsuario());

            // Añadimos el usuario a la sesión
            session.setAttribute("usuario", usuarioEntity);

            return "Encontrado";
        } else {
            return usuarioService.comprobarLogin(usuario);
        }

    }

}