package com.totaltasks.controllers;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/github")
public class GithubController {

	private String getAccessToken(HttpSession session) {
		return (String) session.getAttribute("access_token");
	}

	@GetMapping("/extraerTodo")
	public ResponseEntity<Map<String, Object>> extraerTodo(HttpSession session,
			@RequestParam String owner,
			@RequestParam String repoName) {

		String token = getAccessToken(session);
		if (token == null) return ResponseEntity.status(401).build();

		Map<String, Object> resultado = new HashMap<>();
		resultado.put("commits", getCommits(token, owner, repoName));
		resultado.put("contribuciones", getContribuciones(token, owner, repoName));
		resultado.put("actividadSemanal", getActividadSemanal(token, owner, repoName));
		resultado.put("lenguajes", getLenguajes(token, owner, repoName));
		resultado.put("issues", getIssues(token, owner, repoName));
		resultado.put("pullRequests", getPullRequests(token, owner, repoName));

		return ResponseEntity.ok(resultado);
	}

	@GetMapping("/extraerCommits")
	public ResponseEntity<List<Map<String, Object>>> extraerCommits(HttpSession session,
			@RequestParam String owner,
			@RequestParam String repoName) {

		String token = getAccessToken(session);
		if (token == null) return ResponseEntity.status(401).build();
		return ResponseEntity.ok(getCommits(token, owner, repoName));
	}

	@GetMapping("/stats/contribuciones")
	public ResponseEntity<Object> contribuciones(HttpSession session,
			@RequestParam String owner,
			@RequestParam String repoName) {
		return ResponseEntity.ok(getContribuciones(getAccessToken(session), owner, repoName));
	}

	@GetMapping("/stats/actividadSemanal")
	public ResponseEntity<Object> actividadSemanal(HttpSession session,
			@RequestParam String owner,
			@RequestParam String repoName) {
		return ResponseEntity.ok(getActividadSemanal(getAccessToken(session), owner, repoName));
	}

	@GetMapping("/stats/lenguajes")
	public ResponseEntity<Object> lenguajes(HttpSession session,
			@RequestParam String owner,
			@RequestParam String repoName) {
		return ResponseEntity.ok(getLenguajes(getAccessToken(session), owner, repoName));
	}

	@GetMapping("/issues")
	public ResponseEntity<Object> issues(HttpSession session,
			@RequestParam String owner,
			@RequestParam String repoName) {
		return ResponseEntity.ok(getIssues(getAccessToken(session), owner, repoName));
	}

	@GetMapping("/pullRequests")
	public ResponseEntity<Object> pullRequests(HttpSession session,
			@RequestParam String owner,
			@RequestParam String repoName) {
		return ResponseEntity.ok(getPullRequests(getAccessToken(session), owner, repoName));
	}

	// ===== Métodos privados reutilizables =====

	private List<Map<String, Object>> getCommits(String token, String owner, String repoName) {
		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/commits";
		HttpHeaders headers = createHeaders(token);
		ResponseEntity<List> response = new RestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(headers), List.class);
		return response.getBody();
	}

	private Object getContribuciones(String token, String owner, String repoName) {
		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/stats/contributors";
		return fetchData(token, url);
	}

	private Object getActividadSemanal(String token, String owner, String repoName) {
		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/stats/commit_activity";
		return fetchData(token, url);
	}

	private Object getLenguajes(String token, String owner, String repoName) {
		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/languages";
		return fetchData(token, url);
	}

	private Object getIssues(String token, String owner, String repoName) {
		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/issues?state=all";
		return fetchData(token, url);
	}

	private Object getPullRequests(String token, String owner, String repoName) {
		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/pulls?state=all";
		return fetchData(token, url);
	}

	private HttpHeaders createHeaders(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + token);
		headers.set("Accept", "application/vnd.github+json");
		return headers;
	}

	private Object fetchData(String token, String url) {
		HttpEntity<String> entity = new HttpEntity<>(createHeaders(token));
		ResponseEntity<Object> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, Object.class);
		return response.getBody();
	}
}