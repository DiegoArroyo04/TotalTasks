package com.totaltasks.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.models.UsuarioDTO;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class UsuarioServiceImplementation implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    // OBJETO PARA ENCRIPTAR CONTRASEÑAS
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String registrarUsuario(UsuarioDTO usuario) {

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        // REVISON DE QUE EL USUARIO NO EXISTA
        if (usuarioRepository.findByusuario(usuario.getUsuario()) != null) {
            return "Ya existe un usuario registrado con ese nombre de usuario.";
        } else if (usuarioRepository.findByemail(usuario.getEmail()) != null) {
            return "Ya existe un usuario registrado con ese email.";
        } else {

            // CONVERTIR A ENTITY Y GUARDAR EN BBDD
            usuarioEntity.setNombre(usuario.getNombre());
            usuarioEntity.setUsuario(usuario.getUsuario());
            usuarioEntity.setEmail(usuario.getEmail());

            // ENCRIPTAR CONTRASEÑA
            usuarioEntity.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));

            usuarioRepository.save(usuarioEntity);
            return "Cuenta creada con Éxito.";
        }

    }

    @Override
    public String registrarUsuarioGoogle(UsuarioDTO usuario) {
        UsuarioEntity email = usuarioRepository.findByemail(usuario.getEmail());
        UsuarioEntity user = usuarioRepository.findByemail(usuario.getEmail());

        if (email != null && user != null) {
            // Si el usuario ya existe, solo iniciar sesión
            return "Usuario encontrado. Iniciando sesión...";
        } else {
            // Si no existe, lo registramos
            UsuarioEntity usuarioEntity = new UsuarioEntity();
            usuarioEntity.setNombre(usuario.getNombre());
            usuarioEntity.setUsuario(usuario.getUsuario());
            usuarioEntity.setEmail(usuario.getEmail());

            usuarioRepository.save(usuarioEntity);
            return "Cuenta creada con Éxito.";
        }
    }

    @Override
    public String registrarUsuarioGitHub(UsuarioDTO usuario) {
        UsuarioEntity existente = usuarioRepository.findByemail(usuario.getEmail());
        if (existente != null) {
            return "Usuario encontrado. Iniciando sesión...";
        } else {
            UsuarioEntity nuevoUsuario = new UsuarioEntity();
            nuevoUsuario.setNombre(usuario.getNombre());
            nuevoUsuario.setUsuario(usuario.getUsuario());
            nuevoUsuario.setEmail(usuario.getEmail());
            usuarioRepository.save(nuevoUsuario);
            return "Cuenta creada con Éxito.";
        }
    }

    // Método para obtener el access token de GitHub
    @SuppressWarnings({ "rawtypes", "unchecked" }) // Adevertencia de tipos de datos
    @Override
    public String obtenerAccessTokenDeGitHub(String code) {
        /*
         * Este método envía una solicitud POST a
         * "https://github.com/login/oauth/access_token"
         * usando un RestTemplate. Se le envían tres parámetros:
         * - client_id: el identificador de la aplicación en GitHub.
         * - client_secret: la clave secreta (debe mantenerse confidencial).
         * - code: el código que GitHub nos devuelve en el callback.
         * 
         * Además, se establece el header "Accept" con el valor "application/json" para
         * indicarle a GitHub que queremos recibir la respuesta en formato JSON.
         * 
         * La respuesta es un mapa en el que se extrae el "access_token", que es
         * necesario
         * para realizar futuras solicitudes a la API de GitHub (como obtener los datos
         * del usuario).
         */
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "Ov23li9EsZ9MUsqhPpoX");
        params.add("client_secret", "0b382c7410bfde696afcc987b8423cecd50fa30a");
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Indicamos que la respuesta se espere en formato JSON
        headers.set("Accept", "application/json");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        String tokenUrl = "https://github.com/login/oauth/access_token";

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        return responseBody != null ? (String) responseBody.get("access_token") : null;
    }

    // Método para obtener los datos del usuario de GitHub usando el access token
    @SuppressWarnings({ "rawtypes", "unchecked" }) // Advertencia de tipo de datos
    @Override
    public UsuarioDTO obtenerDatosUsuarioGitHub(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String userUrl = "https://api.github.com/user";
        ResponseEntity<Map> response = restTemplate.exchange(userUrl, HttpMethod.GET, entity, Map.class);

        Map userData = response.getBody();

        // Extraemos los datos relevantes: nombre, login (usuario) y email
        String nombre = (String) userData.get("name");
        String usuario = (String) userData.get("login");
        String email = (String) userData.get("email");

        // Si el email es null, hacer otra petición para obtenerlo
        if (email == null) {
            String emailUrl = "https://api.github.com/user/emails";
            ResponseEntity<List> emailResponse = restTemplate.exchange(emailUrl, HttpMethod.GET, entity, List.class);

            List<Map<String, Object>> emails = emailResponse.getBody();
            if (emails != null) {
                for (Map<String, Object> emailObj : emails) {
                    boolean primary = (boolean) emailObj.get("primary");
                    boolean verified = (boolean) emailObj.get("verified");
                    if (primary && verified) {
                        email = (String) emailObj.get("email");
                        break;
                    }
                }
            }
        }

        // Informacion extra
        String avatarUrl = (String) userData.get("avatar_url");
        String bio = (String) userData.get("bio");
        String blog = (String) userData.get("blog");
        String location = (String) userData.get("location");
        Integer publicRepos = (Integer) userData.get("public_repos");

        System.out.println(
                "Mostrar datos del usuario de GitHub:\n" +
                        "Nombre: " + nombre + "\n" +
                        "Usuario: " + usuario + "\n" +
                        "Email: " + email + "\n" +
                        "Avatar URL: " + avatarUrl + "\n" +
                        "Bio: " + bio + "\n" +
                        "Blog: " + blog + "\n" +
                        "Location: " + location + "\n" +
                        "Public Repos: " + publicRepos);

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombre != null ? nombre : usuario);
        usuarioDTO.setUsuario(usuario);
        usuarioDTO.setEmail(email != null ? email : usuario + "@github.com");

        return usuarioDTO;
    }

    // Obtener datos del repositorio de GITHUB
    @SuppressWarnings({ "rawtypes", "unchecked" }) // Advertencia de tipo de datos
    @Override
    public List<RepoDTO> obtenerRepositoriosUsuarioGitHub(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Configuramos las cabeceras con el token y el header de preview para topics
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github.mercy-preview+json"); // Necesario para obtener topics
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 1. Obtenemos la lista de repositorios del usuario
        String reposUrl = "https://api.github.com/user/repos";
        ResponseEntity<List> response = restTemplate.exchange(reposUrl, HttpMethod.GET, entity, List.class);
        List<Map> reposList = response.getBody();

        List<RepoDTO> repoDTOList = new ArrayList<>();
        if (reposList != null) {
            for (Map repo : reposList) {
                RepoDTO repoDTO = new RepoDTO();
                repoDTO.setName((String) repo.get("name"));
                repoDTO.setFullName((String) repo.get("full_name"));
                repoDTO.setDescription((String) repo.get("description"));
                repoDTO.setHtmlUrl((String) repo.get("html_url"));
                repoDTO.setHomepage((String) repo.get("homepage"));
                repoDTO.setLanguage((String) repo.get("language"));
                repoDTO.setStargazersCount((Integer) repo.get("stargazers_count"));
                repoDTO.setForksCount((Integer) repo.get("forks_count"));
                repoDTO.setWatchersCount((Integer) repo.get("watchers_count"));
                repoDTO.setOpenIssuesCount((Integer) repo.get("open_issues_count"));
                repoDTO.setCreatedAt((String) repo.get("created_at"));
                repoDTO.setUpdatedAt((String) repo.get("updated_at"));
                repoDTO.setPushedAt((String) repo.get("pushed_at"));

                // Los topics vienen como una lista (si están disponibles)
                List<String> topics = (List<String>) repo.get("topics");
                repoDTO.setTopics(topics);

                // 2. Para cada repositorio, obtenemos el detalle de lenguajes usando el
                // languages_url
                String languagesUrl = (String) repo.get("languages_url");
                ResponseEntity<Map> languagesResponse = restTemplate.exchange(languagesUrl, HttpMethod.GET, entity,
                        Map.class);
                Map<String, Integer> languages = languagesResponse.getBody();
                repoDTO.setLanguages(languages);

                repoDTOList.add(repoDTO);
            }
            System.out.println("Repositorios obtenidos: " + repoDTOList);

        }
        return repoDTOList;
    }

    @Override
    public String comprobarLogin(UsuarioDTO usuario) {

        // BUSCAR USUARIO
        UsuarioEntity usuarioEntity = usuarioRepository.findByusuario(usuario.getUsuario());

        if (usuarioEntity == null) {
            return "Usuario no encontrado.Por favor,registrese.";
        } else {

            // COMPROBAR QUE LAS CONTRASEÑAS COINCIDAN COMPARANDO LA DEL USUARIO Y LA
            // ENCRIPTADA EN BBDD
            if (passwordEncoder.matches(usuario.getContrasenia(), usuarioEntity.getContrasenia())) {
                return "Encontrado";
            } else {
                return "La contraseña no coincide.Por favor,vuelve a intentarlo.";
            }

        }

    }

    @Override
    public UsuarioEntity encontrarUsuario(String usuario) {
        // BUSCAR USUARIO
        return usuarioRepository.findByusuario(usuario);
    }

}