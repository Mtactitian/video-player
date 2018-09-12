package com.alexb.bot.config;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class VkConfig {

    @Value("${client.id}")
    private Integer clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${redirect.url}")
    private String redirectUrl;

    @Value("${client.code}")
    private String clientCode;

    @Bean
    public VkApiClient apiClient() {
        return new VkApiClient(new HttpTransportClient());
    }

    @Bean
    public UserActor userActor(VkApiClient vk) throws ClientException, ApiException {
        UserAuthResponse userAuthResponse = vk.oauth().userAuthorizationCodeFlow(clientId, clientSecret,
                redirectUrl, clientCode).execute();
        return new UserActor(userAuthResponse.getUserId(), userAuthResponse.getAccessToken());
    }

}
