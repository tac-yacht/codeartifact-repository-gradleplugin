package com.iyher.gradle.plugin.codeartifact_repository;
import java.net.URI;
import java.util.Set;

import org.gradle.api.Action;
import org.gradle.api.ActionConfiguration;
import org.gradle.api.artifacts.ComponentMetadataSupplier;
import org.gradle.api.artifacts.ComponentMetadataVersionLister;
import org.gradle.api.artifacts.repositories.AuthenticationContainer;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenRepositoryContentDescriptor;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.artifacts.repositories.RepositoryContentDescriptor;
import org.gradle.api.credentials.Credentials;

public class CompositRepositoryExclusiveToCodeartifact implements MavenArtifactRepository, CodeArtifactRepository {

	//MavenArtifactRepository original methods. but, this not used in resolving CodeArtifact.
	
	// @formatter:off
	@Override public Set<URI> getArtifactUrls() { return null; }
	@Override public void artifactUrls(Object... urls) {}
	@Override public void setArtifactUrls(Set<URI> urls) {}
	@Override public void setArtifactUrls(Iterable<?> urls) {}
	@Override public void metadataSources(Action<? super MetadataSources> configureAction) {}
	@Override public MetadataSources getMetadataSources() { return null;}
	@Override public void mavenContent(Action<? super MavenRepositoryContentDescriptor> configureAction) {}
	@Override public String getName() {return null;}
	@Override public void setName(String name) {}
	@Override public void content(Action<? super RepositoryContentDescriptor> configureAction) {}
	@Override public boolean isAllowInsecureProtocol() {return false;}
	@Override public void setAllowInsecureProtocol(boolean allowInsecureProtocol) {}
	@Override public PasswordCredentials getCredentials() {return null;}
	@Override public <T extends Credentials> T getCredentials(Class<T> credentialsType) {return null;}
	@Override public void credentials(Action<? super PasswordCredentials> action) {}
	@Override public <T extends Credentials> void credentials(Class<T> credentialsType, Action<? super T> action) {}
	@Override public void credentials(Class<? extends Credentials> credentialsType) {}
	@Override public void authentication(Action<? super AuthenticationContainer> action) {}
	@Override public AuthenticationContainer getAuthentication() {return null;}
	@Override public void setMetadataSupplier(Class<? extends ComponentMetadataSupplier> rule) {}
	@Override public void setMetadataSupplier(Class<? extends ComponentMetadataSupplier> rule, Action<? super ActionConfiguration> configureAction) {}
	@Override public void setComponentVersionsLister(Class<? extends ComponentMetadataVersionLister> lister) {}
	@Override public void setComponentVersionsLister(Class<? extends ComponentMetadataVersionLister> lister,	Action<? super ActionConfiguration> configureAction) {}
	// @formatter:on

	
	// shared methods
	@Override
	public URI getUrl() {
		return url;
	}

	@Override
	public void setUrl(URI url) {
		this.url = url;
	}

	@Override
	public void setUrl(Object url) {
		this.url = URI.create(url.toString());

	}

	//CodeArtifactRepository original methods
	private URI url;
	private String arn;
	private String repository;
	private String region;
	private String domainName;
	private String domainOwner;

	@Override
	public String getArn() {
		return arn;
	}

	@Override
	public void setArn(String arn) {
		this.arn = arn;
	}

	@Override
	public String getRepository() {
		return repository;
	}

	@Override
	public void setRepository(String repository) {
		this.repository = repository;
	}

	@Override
	public String getRegion() {
		return region;
	}

	@Override
	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String getDomainName() {
		return domainName;
	}

	@Override
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Override
	public String getDomainOwner() {
		return domainOwner;
	}

	@Override
	public void setDomainOwner(String domainOwner) {
		this.domainOwner = domainOwner;
	}

}
