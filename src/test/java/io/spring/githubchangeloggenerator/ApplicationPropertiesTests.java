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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import io.spring.githubchangeloggenerator.ApplicationProperties.IssueSort;
import io.spring.githubchangeloggenerator.ApplicationProperties.Section;
import io.spring.githubchangeloggenerator.github.service.Repository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ApplicationProperties}.
 *
 * @author Phillip Webb
 */
class ApplicationPropertiesTests {

	@Test
	void loadYaml() throws Exception {
		YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
		List<PropertySource<?>> yaml = yamlLoader.load("application",
				new ClassPathResource("test-application.yml", getClass()));
		Binder binder = new Binder(ConfigurationPropertySources.from(yaml));
		ApplicationProperties properties = binder.bind("changelog", ApplicationProperties.class).get();
		Repository repository = properties.getRepository();
		assertThat(repository.owner()).isEqualTo("testorg");
		assertThat(repository.name()).isEqualTo("testrepo");
		List<Section> sections = properties.getSections();
		assertThat(sections).hasSize(2);
		assertThat(sections.get(0).title()).isEqualTo(":star: New Features");
		assertThat(sections.get(0).labels()).containsExactly("enhancement");
		assertThat(sections.get(0).group()).isEqualTo("default");
		assertThat(sections.get(0).sort()).isEqualTo(IssueSort.CREATED);
		assertThat(sections.get(1).title()).isEqualTo("Bugs");
		assertThat(sections.get(1).labels()).containsExactly("bug");
		assertThat(sections.get(1).group()).isEqualTo("test");
		assertThat(sections.get(1).sort()).isNull();
		assertThat(properties.getIssues().getExcludes().getLabels()).containsExactly("hide");
		assertThat(properties.getIssues().getSort()).isEqualTo(IssueSort.TITLE);
		assertThat(properties.getContributors().getTitle()).isEqualTo("Nice one!");
		assertThat(properties.getContributors().getExclude().getNames()).containsExactly("philwebb");
		assertThat(properties.getExternalLinks().get(0).getName()).isEqualTo("Release Notes 1");
		assertThat(properties.getExternalLinks().get(0).getLocation()).isEqualTo("url1");
		assertThat(properties.getExternalLinks().get(1).getName()).isEqualTo("Release Notes 2");
		assertThat(properties.getExternalLinks().get(1).getLocation()).isEqualTo("url2");
	}

}
