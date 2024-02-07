import * as aws from "@pulumi/aws";
import * as pulumi from "@pulumi/pulumi";
import { LambdaJar } from "./lambda-jar";
import * as tags from "./tags-policies";

// Automatically inject tags.
const config = new pulumi.Config();
tags.registerAutoTags({
  "user:Project": pulumi.getProject(),
  "user:Stack": pulumi.getStack(),
  "user:UserId": "malte.gessner",
});

const resourcePrefix = `${pulumi.getProject()}-${pulumi.getStack()}`;

const artifactsBucket = new aws.s3.Bucket("spring-lambda-artifacts", {
  versioning: {
    enabled: true,
  },
});

const lambdaJar = new LambdaJar(
  "spring-with-crac-jar",
  `${resourcePrefix}-jar`,
  "spring-with-crac-serverless-0.0.2-SNAPSHOT-lambda-package.zip",
  artifactsBucket.bucket,
  "de.aclue.blog.springwithcrac.StreamLambdaHandler::handleRequest",
  {
    SPRING_PROFILES_ACTIVE: "",
    JAVA_TOOL_OPTIONS: "-XX:+TieredCompilation -XX:TieredStopAtLevel=1",
  }
);

export const endpointLambdaJar = lambdaJar.endpoint;
