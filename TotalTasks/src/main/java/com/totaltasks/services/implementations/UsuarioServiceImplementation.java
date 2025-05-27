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
import org.springframework.web.multipart.MultipartFile;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.models.UsuarioDTO;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.UsuarioService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class UsuarioServiceImplementation implements UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;

	// Objeto para encriptar contraseñas
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public String registrarUsuario(UsuarioDTO usuario) {

		UsuarioEntity usuarioEntity = new UsuarioEntity();

		// Comprobamos que el usuario existe
		if (usuarioRepository.findByusuario(usuario.getUsuario()) != null) {
			return "Ya existe un usuario registrado con ese nombre de usuario.";
		} else if (usuarioRepository.findByemail(usuario.getEmail()) != null) {
			return "Ya existe un usuario registrado con ese email.";
		} else {

			// Convertir a entity y guardar en BBDD
			usuarioEntity.setNombre(usuario.getNombre());
			usuarioEntity.setUsuario(usuario.getUsuario());
			usuarioEntity.setEmail(usuario.getEmail());

			// Encriptar contraseña
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
	@SuppressWarnings({ "rawtypes" }) // Adevertencia de tipos de datos
	@Override
	public String obtenerAccessTokenDeGitHub(String code) {
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
	@SuppressWarnings({ "rawtypes" }) // Advertencia de tipo de datos
	@Override
	public UsuarioDTO obtenerDatosUsuarioGitHub(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		String userUrl = "https://api.github.com/user";
		ResponseEntity<Map> response = restTemplate.exchange(userUrl, HttpMethod.GET, entity, Map.class);
		Map<String, Object> userData = response.getBody();

		// Imprimir todos los datos obtenidos en consola de forma ordenada
		if (userData != null) {
			userData.forEach((key, value) -> System.out.println(String.format("%-20s : %s", key, value)));
		}

		// Extraer campos específicos para el DTO (con valores por defecto si son nulos)
		String nombre = (String) userData.get("name");
		String usuario = (String) userData.get("login");
		String email = (String) userData.get("email");
		String fotoPerfilGithub = (String) userData.get("avatar_url"); // Foto de perfil

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

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setNombre(nombre != null ? nombre : usuario);
		usuarioDTO.setUsuario(usuario);
		usuarioDTO.setEmail(email != null ? email : usuario + "@github.com");
		usuarioDTO.setFotoPerfilGithub(fotoPerfilGithub);

		return usuarioDTO;
	}

	// Obtener datos del repositorio de GITHUB
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<RepoDTO> obtenerRepositoriosUsuarioGitHub(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.mercy-preview+json");
		HttpEntity<String> entity = new HttpEntity<>(headers);

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

				List<String> topics = (List<String>) repo.get("topics");
				repoDTO.setTopics(topics);

				String languagesUrl = (String) repo.get("languages_url");
				ResponseEntity<Map> languagesResponse = restTemplate.exchange(languagesUrl, HttpMethod.GET, entity,Map.class);
				Map<String, Integer> languages = languagesResponse.getBody();
				repoDTO.setLanguages(languages);

				repoDTOList.add(repoDTO);
			}
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

			// Comprobamos las contraseñas
			if (passwordEncoder.matches(usuario.getContrasenia(), usuarioEntity.getContrasenia())) {
				return "Encontrado";
			} else {
				return "La contraseña no coincide.Por favor,vuelve a intentarlo.";
			}
		}
	}

	@Override
	public UsuarioEntity encontrarUsuario(String email) {
		// Buscar por email
		return usuarioRepository.findByemail(email);
	}

	@Override
	public UsuarioEntity encontrarUsuarioPorUsuario(String usuario) {
		// Buscar por usuario
		return usuarioRepository.findByusuario(usuario);
	}

	// Método para actualizar el usuario en la base de datos
	@Override
	public void actualizarUsuario(String nombre, MultipartFile fotoPerfil, UsuarioEntity usuario) throws IOException {
		usuario.setNombre(nombre);
		// Si hay nueva foto, actualízala
		if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
			usuario.setFotoPerfil(fotoPerfil.getBytes());
		}

		usuarioRepository.save(usuario);
	}

	@Override
	public String convertirByteABase64(byte[] foto) {
		if (foto != null) {
			return Base64.getEncoder().encodeToString(foto);
		} else {
			return null;
		}

	}

	@Override
	public UsuarioEntity obtenerUsuarioPorId(Long id) {
		UsuarioEntity usuario = usuarioRepository.findById(id).orElse(null);
		return usuario;
	}

}