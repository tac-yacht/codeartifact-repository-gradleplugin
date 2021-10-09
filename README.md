# codeartifact-repository-gradleplugin

This is enhanced gradle repository configration for AWS CodeArtifact.

## Getting Started

1. set repository.any style.
2. set credentials.any style.
3. finish!

## set repository

### style A

[GetRepositoryEndpoint](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/codeartifact/get-repository-endpoint.html) request parameters.

```gradle
repositories {
    maven(CodeArtifact) {
        domainName = "domain-name"
        repository = "repo"

        //now required. change optionally coming soon.
        domainOwner = "domain-owner"
        region = "region"
    }
}
```

### style B

ARN. response from [CreateRepository](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/codeartifact/create-repository.html), [DescribeRepository](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/codeartifact/describe-repository.html), etc.

```gradle
repositories {
    maven(CodeArtifact) {
        arn = "arn:aws:codeartifact:us-west-2:111122223333:repository/test-domain/test-repo"
    }
}
```

### style C

URL. response from [GetRepositoryEndpoint](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/codeartifact/get-repository-endpoint.html).

```gradle
repositories {
    maven(CodeArtifact) {
        url = "https://test-domain-111122223333.d.codeartifact.us-west-2.amazonaws.com/npm/test-repo/"
    }
}
```

## set credentials

**warning** ``` credentials(AwsCredentials) ``` is not use.

### style 1

standard style.  
before set env. see [refarence](https://docs.aws.amazon.com/codeartifact/latest/ug/tokens-authentication.html#env-var)

```gradle
repositories {
    maven(CodeArtifact) {
        credentials {
            username = 'aws'
            password = System.env.CODEARTIFACT_AUTH_TOKEN
        }
    }
}
```

### style 2

auto get auth token from awsCredentials.

```gradle
repositories {
    maven(CodeArtifact) {
        credentials(AwsToPasswordCredentials) {
            accessKey = "myAccessKey"
            secretKey = "mySecret"
        }
    }
}
```
### style 3

auto get auth token from awsCredentials.

```gradle
repositories {
    maven(CodeArtifact) {
        credentials(AwsToPasswordCredentials) {
            profile = "myProfile"
        }
    }
}
```

## AWS references
* https://docs.aws.amazon.com/codeartifact/latest/ug/maven-gradle.html
* https://docs.aws.amazon.com/codeartifact/latest/ug/tokens-authentication.html
