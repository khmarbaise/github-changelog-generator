/*
 * Copyright 2018-2022 the original author or authors.
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

package io.spring.githubchangeloggenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import io.spring.githubchangeloggenerator.github.payload.Issue;

/**
 * Manages sections of the changelog report.
 *
 * @author Phillip Webb
 * @author Gary Russell
 */
class ChangelogSections {

	private static final List<ChangelogSection> DEFAULT_SECTIONS = List.of(add(":star: New Features", "enhancement"),
			add(":lady_beetle: Bug Fixes", "bug", "regression"),
			add(":notebook_with_decorative_cover: Documentation", "documentation"),
			add(":hammer: Dependency Upgrades", "dependency-upgrade"));

	private static ChangelogSection add(String title, String... labels) {
		return new ChangelogSection(title, null, null, labels);
	}

	private final List<ChangelogSection> sections;

	ChangelogSections(ApplicationProperties properties) {
		this.sections = adapt(properties);
	}

	private List<ChangelogSection> adapt(ApplicationProperties properties) {
		List<ApplicationProperties.Section> propertySections = properties.getSections();
		if (CollectionUtils.isEmpty(propertySections)) {
			return DEFAULT_SECTIONS;
		}
		List<ChangelogSection> customSections = propertySections.stream().map(this::adapt).collect(Collectors.toList());
		if (properties.isAddSections()) {
			List<ChangelogSection> merged = new ArrayList<>(DEFAULT_SECTIONS);
			merged.addAll(customSections);
			return merged;
		}
		return customSections;
	}

	private ChangelogSection adapt(ApplicationProperties.Section propertySection) {
		return new ChangelogSection(propertySection.title(), propertySection.group(), propertySection.sort(),
				propertySection.labels());
	}

	Map<ChangelogSection, List<Issue>> collate(List<Issue> issues) {
		SortedMap<ChangelogSection, List<Issue>> collated = new TreeMap<>(Comparator.comparing(this.sections::indexOf));
		for (Issue issue : issues) {
			for (ChangelogSection section : getSections(issue)) {
				collated.computeIfAbsent(section, (key) -> new ArrayList<>());
				collated.get(section).add(issue);
			}
		}
		return collated;
	}

	private List<ChangelogSection> getSections(Issue issue) {
		List<ChangelogSection> result = new ArrayList<>();
		Set<String> groupClaimes = new HashSet<>();
		for (ChangelogSection section : this.sections) {
			if (section.isMatchFor(issue) && groupClaimes.add(section.getGroup())) {
				result.add(section);
			}
		}
		return result;
	}

}
