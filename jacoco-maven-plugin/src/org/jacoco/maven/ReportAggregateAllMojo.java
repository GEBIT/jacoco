/*******************************************************************************
 * Copyright (c) 2009, 2024 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    John Oliver, Marc R. Hoffmann, Jan Wloka - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.maven;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

/**
 * <p>
 * Creates a structured code coverage report (HTML, XML, and CSV) from multiple
 * projects within reactor. The report is created from all modules this project
 * depends on, and optionally this project itself. From those projects class and
 * source files as well as JaCoCo execution data files will be collected. In
 * addition execution data is collected from the project itself. This also
 * allows to create coverage reports when tests are in separate projects than
 * the code under test, for example in case of integration tests.
 * </p>
 *
 * <p>
 * Using the dependency scope allows to distinguish projects which contribute
 * execution data but should not become part of the report:
 * </p>
 *
 * <ul>
 * <li><code>compile</code>, <code>runtime</code>, <code>provided</code>:
 * Project source and execution data is included in the report.</li>
 * <li><code>test</code>: Only execution data is considered for the report.</li>
 * </ul>
 *
 * @since 0.8.13
 */
@Mojo(name = "report-aggregate-all", threadSafe = true, aggregator = true)
public class ReportAggregateAllMojo extends ReportAggregateMojo {

	@Override
	protected List<MavenProject> findDependencies(final String... scopes) {
		final List<MavenProject> result = reactorProjects;
		if (!includeCurrentProject) {
			result.remove(project);
		}

		// need to exclude pom projects
		final List<MavenProject> nonPomProjects = new ArrayList<>();
		for (final MavenProject mavenProject : result) {
			if (!StringUtils.equals("pom", mavenProject.getPackaging())) {
				nonPomProjects.add(mavenProject);
			}
		}
		return nonPomProjects;
	}
}
