/*
 * Copyright 2018-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.githubchangeloggenerator.github.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Details of a GitHub issue.
 *
 * @author Madhura Bhave
 * @param number The issue number.
 * @param title The title of the issue.
 * @param user The user of the issue.
 * @param labels The labels of this issue.
 * @param url The url of the issue
 * @param pullRequest The related pull request
 * @param body The body of the issue.
 */
public record Issue(@JsonProperty("number") String number, @JsonProperty("title") String title,
		@JsonProperty("user") User user, @JsonProperty("labels") List<Label> labels,
		@JsonProperty("html_url") String url, @JsonProperty("pull_request") PullRequest pullRequest,
		@JsonProperty("body") String body) {

	@Override
	public String toString() {
		return this.title;
	}

}
