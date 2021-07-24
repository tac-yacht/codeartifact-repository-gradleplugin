package com.iyher.gradle.plugin.codeartifact_repository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URI;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class CodeArtifactResolverTest {
	
	private static String[] TEST_FIELDS = {
		"arn",
		"domainName",
		"domainOwner",
		"region",
		"repository",
		"url",
	};
	
	private static Project project = mock(Project.class);
	private static CodeArtifactResolver target = new CodeArtifactResolver(project);
	
	@BeforeAll
	public static void beforeAll() {
		when(project.getRepositories()).thenReturn(mock(RepositoryHandler.class));
	}
	
	@Nested
	class parseResource {
		@Test
		void allParam() {
			CodeArtifactRepository arg = new CompositRepositoryExclusiveToCodeartifact();
			arg.setArn("arn");
			arg.setDomainName("domainname");
			arg.setDomainOwner("domainowner");
			arg.setRegion("region");
			arg.setRepository("repository");
			arg.setUrl(URI.create("https://example.com"));

			CodeArtifactRepository result = target.parseResource(arg);


			CodeArtifactRepository assertMock = expectedMock(
				"arn",
				"domainname",
				"domainowner",
				"region",
				"repository",
				URI.create("https://example.com")
			);
			
			assertThat(result).isEqualToComparingOnlyGivenFields(assertMock, TEST_FIELDS);
		}
		
		@Test
		void paramOnly() {
			CodeArtifactRepository arg = new CompositRepositoryExclusiveToCodeartifact();
			arg.setDomainName("domainname");
			arg.setDomainOwner("domainowner");
			arg.setRegion("region");
			arg.setRepository("repository");

			CodeArtifactRepository result = target.parseResource(arg);


			CodeArtifactRepository assertMock = expectedMock(
				null,
				"domainname",
				"domainowner",
				"region",
				"repository",
				URI.create("https://domainname-domainowner.d.codeartifact.region.amazonaws.com/maven/repository/")
			);
			
			assertThat(result).isEqualToComparingOnlyGivenFields(assertMock, TEST_FIELDS);
		}

		@Test
		void arnOnly() {
			CodeArtifactRepository arg = new CompositRepositoryExclusiveToCodeartifact();
			arg.setArn("arn:aws:codeartifact:us-west-2:111122223333:repository/test-domain/test-repo");

			CodeArtifactRepository result = target.parseResource(arg);


			CodeArtifactRepository assertMock = expectedMock(
				"arn:aws:codeartifact:us-west-2:111122223333:repository/test-domain/test-repo",
				"test-domain",
				"111122223333",
				"us-west-2",
				"test-repo",
				URI.create("https://test-domain-111122223333.d.codeartifact.us-west-2.amazonaws.com/maven/test-repo/")
			);
			
			assertThat(result).isEqualToComparingOnlyGivenFields(assertMock, TEST_FIELDS);
		}

		@Test
		void urlOnly() {
			CodeArtifactRepository arg = new CompositRepositoryExclusiveToCodeartifact();
			arg.setUrl(URI.create("https://my-domain-111122223333.d.codeartifact.us-west-2.amazonaws.com/maven/my_repo/"));

			CodeArtifactRepository result = target.parseResource(arg);


			CodeArtifactRepository assertMock = expectedMock(
				null,
				"my-domain",
				"111122223333",
				"us-west-2",
				"my_repo",
				URI.create("https://my-domain-111122223333.d.codeartifact.us-west-2.amazonaws.com/maven/my_repo/")
			);
			
			assertThat(result).isEqualToComparingOnlyGivenFields(assertMock, TEST_FIELDS);
		}
		
		//TODO 重複優先度のテスト
	}
	
	
	
	private static CodeArtifactRepository expectedMock(String arn, String domainName, String domainOwner, String region, String repository, URI url) {
		CodeArtifactRepository mock = mock(CodeArtifactRepository.class);

		when(mock.getArn()).thenReturn(arn);
		when(mock.getDomainName()).thenReturn(domainName);
		when(mock.getDomainOwner()).thenReturn(domainOwner);
		when(mock.getRegion()).thenReturn(region);
		when(mock.getRepository()).thenReturn(repository);
		when(mock.getUrl()).thenReturn(url);
		
		return mock;
	}
}
