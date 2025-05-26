package com.totaltasks.controllers;

import java.util.List;
import java.util.Map;

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

	@GetMapping("/extraerCommits")
	public ResponseEntity<String> extraerCommits(HttpSession session,
			@RequestParam String owner,
			@RequestParam String repoName) {

		String token = getAccessToken(session);
		if (token == null) return ResponseEntity.status(401).body("Token no disponible.");

		String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/commits";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + token);
		headers.set("Accept", "application/vnd.github+json");

		ResponseEntity<List> response = new RestTemplate().exchange(
				url, HttpMethod.GET, new HttpEntity<>(headers), List.class);

		StringBuilder result = new StringBuilder("===== Commits =====<br>");
		List<Map<String, Object>> commits = response.getBody();

		if (commits == null || commits.isEmpty()) {
			return ResponseEntity.ok("No se encontraron commits.");
		}

		for (Map<String, Object> commitObj : commits) {
			Map<String, Object> commit = (Map<String, Object>) commitObj.get("commit");
			Map<String, Object> author = (Map<String, Object>) commit.get("author");
			Map<String, Object> ghAuthor = (Map<String, Object>) commitObj.get("author");

			result.append("Mensaje: ").append(commit.get("message")).append("<br>");
			result.append("Autor: ").append(author.get("name")).append(" (").append(author.get("email")).append(")<br>");
			result.append("Fecha: ").append(author.get("date")).append("<br>");

			if (ghAuthor != null && ghAuthor.get("login") != null) {
				result.append("GitHub: ").append(ghAuthor.get("login")).append("<br>");
			}

			result.append("SHA: ").append(commitObj.get("sha")).append("<br>");
			result.append("<a href='").append(commitObj.get("html_url")).append("' target='_blank'>Ver en GitHub</a><br>");
			result.append("--------------------------------<br>");
		}

		return ResponseEntity.ok(result.toString());
	}
}