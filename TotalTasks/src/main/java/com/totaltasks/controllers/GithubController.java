package com.totaltasks.controllers;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    /* 
     * MÉTODO PARA CREAR REPO
     */
    @PostMapping("/crearRepo")
    public ResponseEntity<String> crearRepositorio(
            @RequestParam String accessToken, 
            @RequestParam String repoName, 
            @RequestParam String description, 
            @RequestParam boolean isPrivate) {
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github+json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construir el JSON de la solicitud
        Map<String, Object> body = new HashMap<>();
        body.put("name", repoName);
        body.put("description", description);
        body.put("private", isPrivate);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        String url = "https://api.github.com/user/repos";

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Repositorio '" + repoName + "' creado exitosamente.");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Error al crear el repositorio: " + response.getStatusCode());
        }
    }

    /* 
     * MÉTODO PARA CREAR ARCHIVO
     */
    @PostMapping("/crearArchivo")
    public ResponseEntity<String> crearArchivo(
            @RequestParam String accessToken, 
            @RequestParam String owner, 
            @RequestParam String repoName, 
            @RequestParam String filePath, 
            @RequestParam String contentStr, 
            @RequestParam String commitMessage) {
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github+json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Codificar el contenido en Base64
        String encodedContent = Base64.getEncoder().encodeToString(contentStr.getBytes());

        // Construir el JSON de la solicitud
        Map<String, Object> body = new HashMap<>();
        body.put("message", commitMessage);
        body.put("content", encodedContent);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/contents/" + filePath;

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Archivo '" + filePath + "' creado exitosamente.");
        } else {
            return ResponseEntity.status(response.getStatusCode())
                    .body("Error al crear el archivo: " + response.getStatusCode());
        }
    }

    /* 
     * MÉTODO PARA ACTUALIZAR ARCHIVO
     */
    @PostMapping("/actualizarArchivo")
    public ResponseEntity<String> actualizarArchivo(
            @RequestParam String accessToken, 
            @RequestParam String owner, 
            @RequestParam String repoName, 
            @RequestParam String filePath, 
            @RequestParam String nuevoContenido, 
            @RequestParam String commitMessage) {
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github+json");
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        // Obtener el SHA del archivo actual
        String getUrl = "https://api.github.com/repos/" + owner + "/" + repoName + "/contents/" + filePath;
        HttpEntity<String> getEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> getResponse = restTemplate.exchange(getUrl, HttpMethod.GET, getEntity, Map.class);
        Map<String, Object> fileData = getResponse.getBody();
        if (fileData == null || fileData.get("sha") == null) {
            return ResponseEntity.badRequest().body("No se encontró el archivo o no se pudo obtener el SHA.");
        }
        String sha = (String) fileData.get("sha");
    
        // Codificar el nuevo contenido en Base64
        String encodedContent = Base64.getEncoder().encodeToString(nuevoContenido.getBytes());
    
        // Construir el JSON de la solicitud de actualización
        Map<String, Object> body = new HashMap<>();
        body.put("message", commitMessage);
        body.put("content", encodedContent);
        body.put("sha", sha);
    
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(getUrl, HttpMethod.PUT, entity, Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Archivo '" + filePath + "' actualizado exitosamente.");
        } else {
            return ResponseEntity.status(response.getStatusCode())
                    .body("Error al actualizar el archivo: " + response.getStatusCode());
        }
    }
    
    /* 
     * MÉTODO PARA EXTRAER COMMITS
     */
    @GetMapping("/extraerCommits")
    public ResponseEntity<String> extraerCommits(
            @RequestParam String accessToken, 
            @RequestParam String owner, 
            @RequestParam String repoName) {
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github+json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
    
        String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/commits";
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        List<Map> commits = response.getBody();
    
        StringBuilder result = new StringBuilder("===== Commits en el repositorio =====<br>");
        if (commits != null) {
            for (Map commitObj : commits) {
                Map commitDetail = (Map) commitObj.get("commit");
                result.append("Mensaje: ").append(commitDetail.get("message")).append("<br>")
                      .append("Fecha: ").append(((Map)commitDetail.get("author")).get("date")).append("<br>")
                      .append("-------------------------------------<br>");
            }
        } else {
            result.append("No se encontraron commits.");
        }
        return ResponseEntity.ok(result.toString());
    }
}