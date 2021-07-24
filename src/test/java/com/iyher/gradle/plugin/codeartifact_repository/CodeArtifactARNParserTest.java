package com.iyher.gradle.plugin.codeartifact_repository;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CodeArtifactARNParserTest {

	@Test
	void testParse() {
		CodeArtifactRepository result = CodeArtifactARNParser.parse("arn:aws:codeartifact:us-west-2:111122223333:repository/test-domain/test-repo");
		
		assertThat(result.getDomainName()).isEqualTo("test-domain");
		assertThat(result.getRegion()).isEqualTo("us-west-2");
		assertThat(result.getRepository()).isEqualTo("test-repo");
	}
}
