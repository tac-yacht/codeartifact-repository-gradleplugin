package com.iyher.gradle.plugin.codeartifact_repository;
import static org.assertj.core.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CodeArtifactURLParserTest {

	@Test
	void testParse() {
		CodeArtifactRepository result = CodeArtifactURLParser.parse(URI.create("https://my-domain-111122223333.d.codeartifact.us-west-2.amazonaws.com/maven/my_repo/"));
		
		assertThat(result.getDomainName()).isEqualTo("my-domain");
		assertThat(result.getDomainOwner()).isEqualTo("111122223333");
		assertThat(result.getRegion()).isEqualTo("us-west-2");
		assertThat(result.getRepository()).isEqualTo("my_repo");
	}
	
	@Nested
	class hostParse {
		@Test
		void success() {
			CodeArtifactRepository result = new CompositRepositoryExclusiveToCodeartifact();
			CodeArtifactURLParser.hostParse("my-domain-111122223333.d.codeartifact.us-west-2.amazonaws.com", result);
			
			assertThat(result.getDomainName()).isEqualTo("my-domain");
			assertThat(result.getDomainOwner()).isEqualTo("111122223333");
			assertThat(result.getRegion()).isEqualTo("us-west-2");
		}
	}

	@Nested
	class pathParse {
		@Test
		void success() {
			CodeArtifactRepository result = new CompositRepositoryExclusiveToCodeartifact();
			CodeArtifactURLParser.pathParse("/maven/my_repo/", result);
			
			assertThat(result.getRepository()).isEqualTo("my_repo");
		}
	}
}
