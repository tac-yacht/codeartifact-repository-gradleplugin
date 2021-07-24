package com.iyher.gradle.plugin.codeartifact_repository;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Closure;

public class CodeArtifactResolver {
	private static final Logger log = LoggerFactory.getLogger(CodeArtifactResolver.class);
	private static final class ClosureLog{}
	
	@SuppressWarnings("unused")
	private final RepositoryHandler repositories;
	private final Project project;

	public CodeArtifactResolver(Project project) {
		this.project = project;
		this.repositories = project.getRepositories();
	}
	
	public Closure<Void> resolve(Closure<?> closure) {
		@SuppressWarnings("unused")
		CodeArtifactRepository repo = parseResource(closure);
		
		return new Closure<Void>(null) {
			private final Logger innerlog = LoggerFactory.getLogger(ClosureLog.class);
			@SuppressWarnings("unused")
			public void doCall(Object arg) {
				innerlog.debug("extra closure");
			}
		};
	}
	
	CodeArtifactRepository parseResource(Closure<?> closure) {
		log.debug("start: convert closure to codeartifact configuration");
		CodeArtifactRepository base = (CodeArtifactRepository) project.configure(new CompositRepositoryExclusiveToCodeartifact(), closure);
		log.debug("end  : convert closure to codeartifact configuration");
		
		return base;
	}
}
