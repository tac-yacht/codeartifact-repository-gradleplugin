# codeartifact-repository-gradleplugin

This is enhanced gradle repository configration for AWS CodeArtifact.

## Getting Started

### set repository

#### style A

[GetRepositoryEndpoint](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/codeartifact/get-repository-endpoint.html) request parameters.

```gradle
repository {
    maven(CodeArtifact) {
        domainName = "domain-name"
        repository = "repo"

        //now required. change optionally coming soon.
        domainOwner = "domain-owner"
        region = "region"
    }
}
```

#### style B

ARN. response from CreateRepository, DescribeRepository, etc.

```gradle
repository {
    maven(CodeArtifact) {
        arn = "arn:aws:codeartifact:us-west-2:111122223333:repository/test-domain/test-repo"
    }
}
```

#### style C

URL. response from GetRepositoryEndpoint.

```gradle
repository {
    maven(CodeArtifact) {
        url = "https://test-domain-111122223333.d.codeartifact.us-west-2.amazonaws.com/npm/test-repo/"
    }
}
```

### credentials

coming soon
