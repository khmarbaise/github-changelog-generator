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

package io.spring.githubchangeloggenerator.github.service;

import org.springframework.util.Assert;

/**
 * A reference to a specific GitHub repository.
 *
 * @author Phillip Webb
 * @param owner The owner of the repository.
 * @param name The name of the repository.
 */
public record Repository(String owner, String name) {

	@Override
	public String toString() {
		return this.owner + "/" + this.name;
	}

	public static Repository of(String reference) {
		Assert.hasText(reference, "GitHub repository references must not be empty");
		int slashIndex = reference.indexOf('/');
		Assert.isTrue(slashIndex >= 0, "GitHub repository references must include '/'");
		return new Repository(reference.substring(0, slashIndex), reference.substring(slashIndex + 1));
	}

}
