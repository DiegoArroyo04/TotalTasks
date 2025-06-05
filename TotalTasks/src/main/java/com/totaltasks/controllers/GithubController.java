package com.totaltasks.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/github")
public class GithubController {

	private String getAccessToken(HttpSession session) {
		return (String) session.getAttribute("access_token");
	}

	@GetMapping("/extraerTodo")
	public ResponseEntity<Map<String, Object>> extraerTodo(HttpSession session, @RequestParam String owner,
			@RequestParam String repoName) {

		String token = getAccessToken(session);
		if (token == null)
			return ResponseEntity.status(401).build();

		Map<String, Object> resultado = new HashMap<>();
		resultado.put("commits", getCommits(token, owner, repoName));
		resultado.put("lenguajes", getLenguajes(token, owner, repoName));

		return ResponseEntity.ok(resultado);
	}

	@GetMapping("/extraerCommits")
	public ResponseEntity<List<Map<String, Object>>> extraerCommits(HttpSession session, @RequestParam String owner,
			@RequestParam String repoName) {
		String token = getAccessToken(session);
		if (token == null)
			return ResponseEntity.status(401).build();
		return ResponseEntity.ok(getCommits(token, owner, repoName));
	}

	@GetMapping("/stats/lenguajes")
	public ResponseEntity<Object> lenguajes(HttpSession session, @RequestParam String owner,
			@RequestParam String repoName) {
		return ResponseEntity.ok(getLenguajes(getAccessToken(session), owner, repoName));
	}

	@GetMapping("/branches")
	public ResponseEntity<Object> branches(HttpSession session, @RequestParam String owner,
			@RequestParam String repoName) {
		String token = getAccessToken(session);
		if (token == null)
			return ResponseEntity.status(401).build();
		return ResponseEntity.ok(getBranches(token, owner, repoName));
	}

	// ===== Métodos privados reutilizables =====

	private List<Map<String, Object>> getCommits(String token, String owner, String repoName) {

		List<Map<String, Object>> allCommits = new ArrayList<>();
		HttpHeaders headers = createHeaders(token);
		RestTemplate restTemplate = new RestTemplate();

		int perPage = 100;
		int page = 1;

		while (true) {
			String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/commits?per_page=" + perPage
					+ "&page=" + page;
			ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
					List.class);

			List<Map<String, Object>> commitsPage = response.getBody();
			if (commitsPage == null || commitsPage.isEmpty())
				break;

			allCommits.addAll(commitsPage);

			// Si se devolvieron menos de 100, es la última página
			if (commitsPage.size() < perPage)
				break;

			page++;
		}

		return allCommits;
	}

	private Object getLenguajes(String token, String owner, String repoName) {
		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/languages";
		return fetchData(token, url);
	}

	private Object getBranches(String token, String owner, String repoName) {
		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/branches";
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