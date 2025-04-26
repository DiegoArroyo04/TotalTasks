package com.totaltasks.models;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepoDTO {

	private String name;
	private String fullName;
	private String description;
	private String htmlUrl;
	private String homepage;
	private String language;
	private List<String> topics;
	private Integer stargazersCount;
	private Integer forksCount;
	private Integer watchersCount;
	private Integer openIssuesCount;
	private String createdAt;
	private String updatedAt;
	private String pushedAt;
	private Map<String, Integer> languages;

}