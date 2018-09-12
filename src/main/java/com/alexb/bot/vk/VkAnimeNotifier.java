package com.alexb.bot.vk;

import com.alexb.bot.model.AnimeSubscription;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VkAnimeNotifier {

    private final VkApiClient vkApiClient;
    private final UserActor userActor;
    private final AnimeSubscription animeSubscription;

    public void send(@Body String message) throws ClientException, ApiException, InterruptedException {
        for (int userId : animeSubscription.getRecipientsIds()) {
            vkApiClient.messages()
                    .send(userActor)
                    .userId(userId)
                    .message(message)
                    .execute();
            log.debug("Sent message to {}", userId);

            Thread.sleep(1000); //Vk api does not allow more than 3 requests per second

        }
    }

}
