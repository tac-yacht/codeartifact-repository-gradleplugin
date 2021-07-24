package com.iyher.gradle.plugin.codeartifact_repository;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
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
		CodeArtifactRepository repo = parseResource(closure);
		
		final URI url = repo.getUrl();
		
		return new Closure<Void>(null) {
			private final Logger innerlog = LoggerFactory.getLogger(ClosureLog.class);
			@SuppressWarnings("unused")
			public void doCall(Object arg) {
				innerlog.debug("extra closure");
				
				MavenArtifactRepository repo = (MavenArtifactRepository)getDelegate();
				repo.setUrl(url);
			}
		};
	}

	CodeArtifactRepository parseResource(Closure<?> closure) {
		log.debug("start: convert closure to codeartifact configuration");
		CodeArtifactRepository origin = (CodeArtifactRepository) project.configure(new CompositRepositoryExclusiveToCodeartifact(), closure);
		log.debug("end  : convert closure to codeartifact configuration");

		return parseResource(origin);
	}

	CodeArtifactRepository parseResource(CodeArtifactRepository origin) {
		if(!isValid(origin)) {
			throw new IllegalStateException("url or arn or (region and domain and repository) required");
		}
		
		if (allNotNull(origin.getDomainName(), origin.getDomainOwner(), origin.getRegion(),
			origin.getRepository(),origin.getUrl()
		)) {
			log.debug("has all params");
			return origin;
		}

		Optional<CodeArtifactRepository> urlBase = Optional.of(origin)
			.map(CodeArtifactRepository::getUrl)
			.map(CodeArtifactURLParser::parse)
		;

		Optional<CodeArtifactRepository> arnBase = Optional.of(origin)
			.map(CodeArtifactRepository::getArn)
			.map(CodeArtifactARNParser::parse)
			.map(element -> {
				URI url = CodeArtifactURLFinder.find(element);
				element.setUrl(url);
				return element;
			})
		;

		Optional<CodeArtifactRepository> paramBase = Optional.of(origin)
			.map(CodeArtifactURLFinder::find)
			.map(url -> {
				CodeArtifactRepository result = new CompositRepositoryExclusiveToCodeartifact();
				result.setUrl(url);
				return result;
			})
		;
		

		CodeArtifactRepository result = new CompositRepositoryExclusiveToCodeartifact();
		mapAndFindFirst(CodeArtifactRepository::getUrl, Optional.of(origin), urlBase, arnBase, paramBase)
			.ifPresent(result::setUrl);

		mapAndFindFirst(CodeArtifactRepository::getDomainName, Optional.of(origin), urlBase, arnBase, paramBase)
			.ifPresent(result::setDomainName);
		mapAndFindFirst(CodeArtifactRepository::getDomainOwner, Optional.of(origin), urlBase, arnBase, paramBase)
			.ifPresent(result::setDomainOwner);
		mapAndFindFirst(CodeArtifactRepository::getRegion, Optional.of(origin), urlBase, arnBase, paramBase)
			.ifPresent(result::setRegion);
		mapAndFindFirst(CodeArtifactRepository::getRepository, Optional.of(origin), urlBase, arnBase, paramBase)
			.ifPresent(result::setRepository);

		mapAndFindFirst(CodeArtifactRepository::getArn, Optional.of(origin), urlBase, arnBase, paramBase)
			.ifPresent(result::setArn);
	
		
		return result;
	}
	
	boolean isValid(CodeArtifactRepository repo) {
		if (allNotNull(repo.getUrl())) {
			return true;
		}

		if (allNotNull(repo.getArn())) {
			return true;
		}
		
		if (allNotNull(repo.getDomainName(), repo.getRepository())) {
			if (allNotNull(repo.getRegion(), repo.getDomainOwner())) {
				return true;
			} else {
				//TODO 動的解決できるところなのであとで対応
				log.warn("now unsupported. dynamic resolve is future operation.");
				return false;
			}
		}
		
		return false;
	}
	
	@SafeVarargs
	static final <T,R> Optional<R> mapAndFindFirst(Function<T,R> map, Optional<T> ... opts) {
		Function<Optional<T>, Optional<R>> wappedFunction = t -> t.map(map);
		return Stream.of(opts)
			.map(wappedFunction)
			.filter(Optional::isPresent)
			.map(Optional::get)
		.findFirst();
	}
}
