package com.spoctexter.secrets;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.spoctexter.jwt.JwtConfig;
import com.spoctexter.twilio.TwilioConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;

@Configuration
public class SecretsConfiguration {

    @Resource
    private Environment env;

    private static final Logger log = LoggerFactory.getLogger(SecretsConfiguration.class);

    @Bean
    @Primary
    @ConfigurationProperties("spoctexter")
    public JwtConfig jwtProperties() {
        JsonNode secretsJson = getSecrets();
        String secretKey = secretsJson.get("secretKey").textValue();
        String tokenPrefix = secretsJson.get("tokenPrefix").textValue();
        Integer tokenExpirationAfterDays = Integer.valueOf(secretsJson.get("tokenExpirationAfterDays").asInt());

        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecretKey(secretKey);
        jwtConfig.setTokenPrefix(tokenPrefix);
        jwtConfig.setTokenExpirationAfterDays(tokenExpirationAfterDays);
        return jwtConfig;
    }

    @Bean
    @Primary
    @ConfigurationProperties("twilio")
    public TwilioConfiguration twilioProperties() {
        JsonNode secretsJson = getSecrets();
        String accountSid = secretsJson.get("accountSid").textValue();
        String authToken = secretsJson.get("authToken").textValue();
        String trialNumber = secretsJson.get("trialNumber").textValue();

        TwilioConfiguration twilioConfiguration = new TwilioConfiguration();
        twilioConfiguration.setAccountSid(accountSid);
        twilioConfiguration.setAuthToken(authToken);
        twilioConfiguration.setTrialNumber(trialNumber);

        return twilioConfiguration;
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties appDataSourceProperties() {

        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource appDataSource() {
        JsonNode secretsJson = getSecrets();

        String host = secretsJson.get("host").textValue();
        String port = secretsJson.get("port").textValue();
        String dbname = secretsJson.get("dbname").textValue();
        String username = secretsJson.get("username").textValue();
        String password = secretsJson.get("password").textValue();

        appDataSourceProperties().setUrl("jdbc:postgresql://" + host + ":" + port + "/" + dbname);
        appDataSourceProperties().setUsername(username);
        appDataSourceProperties().setPassword(password);
        return appDataSourceProperties().initializeDataSourceBuilder().build();

    }

    public JsonNode getSecrets() {

        String secretName = env.getProperty("aws.secretName");
        String region = env.getProperty("aws.region");
        String accessKey = env.getProperty("aws.accessKey");
        String secretKey = env.getProperty("aws.secretKey");

        // Create a Secrets Manager client
        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode secretsJson = null;

        ByteBuffer binarySecretData;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResponse = null;
        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);

        } catch(ResourceNotFoundException e) {
            log.error("The requested secret " + secretName + " was not found");
        } catch (InvalidRequestException e) {
            log.error("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            log.error("The request had invalid params: " + e.getMessage());
        }

        if(getSecretValueResponse == null) {
            return null;
        }

        // Decrypted secret using the associated KMS CMK
        // Depending on whether the secret was a string or binary, one of these fields will be populated
        String secret = getSecretValueResponse.getSecretString();
        if(secret == null) {
            log.error("The Secret String returned is null");
            return null;
        }
        try {
            secretsJson = objectMapper.readTree(secret);
//            System.out.println("Secrets json - "+secretsJson);
            return secretsJson;
        } catch (IOException e) {
            log.error("Exception while retrieving secret values: " + e.getMessage());
        }

        return null;
    }
}

