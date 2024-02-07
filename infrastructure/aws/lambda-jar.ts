import * as aws from "@pulumi/aws";
import * as pulumi from "@pulumi/pulumi";

export class LambdaJar extends pulumi.ComponentResource {
  public readonly endpoint: pulumi.Output<string>;

  constructor(
    name: string,
    resourcePrefix: string,
    artifactName: string,
    artifactsBucketName: pulumi.Output<string>,
    functionHandlerName: string,
    functionEnvironmentVariables: {},
    lambdaLayers?: string[],
    opts?: pulumi.ComponentResourceOptions
  ) {
    super("de.aclue.techlabor:LambdaJar", name, {}, opts);

    // Configure IAM so that the AWS Lambda can be run.
    const springLambdaRole = new aws.iam.Role(`${resourcePrefix}lambda-role`, {
      assumeRolePolicy: {
        Version: "2012-10-17",
        Statement: [
          {
            Action: "sts:AssumeRole",
            Principal: {
              Service: "lambda.amazonaws.com",
            },
            Effect: "Allow",
            Sid: "",
          },
        ],
      },
    });
    new aws.iam.RolePolicyAttachment(
      `${resourcePrefix}lambda-role-policy-attach`,
      {
        role: springLambdaRole,
        policyArn: aws.iam.ManagedPolicies.AWSLambdaBasicExecutionRole,
      }
    );

    // Upload java function code to S3
    const springBootArtifact = new aws.s3.BucketObject(
      `${resourcePrefix}artifact`,
      {
        bucket: artifactsBucketName,
        source: new pulumi.asset.FileAsset(`../../app/target/${artifactName}`), // path to your jar file
      }
    );

    // Create the Spring Native Lambda Function using the custom Docker image.
    const springLambda = new aws.lambda.Function(
      `${resourcePrefix}spring-app-lambda`,
      {
        packageType: "Zip",
        layers: lambdaLayers,
        handler: functionHandlerName,
        s3Bucket: springBootArtifact.bucket,
        s3Key: springBootArtifact.key,
        s3ObjectVersion: springBootArtifact.versionId,
        runtime: aws.lambda.Runtime.Java21,
        role: springLambdaRole.arn,
        memorySize: 256,
        timeout: 10,
        snapStart: {
          applyOn: "PublishedVersions",
        },
        environment: {
          variables: functionEnvironmentVariables,
        },
        publish: true,
      }
    );

    const lambdaAlias = new aws.lambda.Alias(
      `${resourcePrefix}spring-app-alias`,
      {
        functionName: springLambda.arn,
        functionVersion: springLambda.version,
      }
    );

    const lambdaAliasUrl = new aws.lambda.FunctionUrl(
      `${resourcePrefix}lambda-alias-function-url`,
      {
        functionName: lambdaAlias.functionName,
        qualifier: lambdaAlias.name,
        authorizationType: "NONE",
      }
    );

    this.endpoint = lambdaAliasUrl.functionUrl;
  }
}
