package com.alexb.bot.processors;

import com.alexb.bot.model.AnimeSubscription;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Dialog;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.responses.GetDialogsResponse;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class VkSubscriptionProcessor implements Processor {
    private final AnimeSubscription animeSubscription;
    private final VkApiClient vkApiClient;
    private final UserActor userActor;

    @Override
    public void process(Exchange exchange) throws Exception {
        GetDialogsResponse dialogsResponse = vkApiClient.messages()
                .getDialogs(userActor)
                .unread(true)
                .execute();

        List<Dialog> dialogs = dialogsResponse.getItems();
        Set<Integer> recipientsIds = animeSubscription.getRecipientsIds();

        for (Dialog dialog : dialogs) {
            Message message = dialog.getMessage();
            int userId = message.getUserId();

            if (message.getBody().toLowerCase().contains("subscribe me")) {
                if (recipientsIds.add(userId)) {
                    sendMessage(userId, "Ok, now you are subscribed!");
                }
                if (message.getBody().toLowerCase().contains("unsubscribe me")) {
                    if (recipientsIds.remove(userId)) {
                        sendMessage(userId, "Ok, now you are unsubscribed!");
                    }
                }
            }

        }
    }

    private void sendMessage(int to, String message) throws ClientException, ApiException {
        vkApiClient.messages()
                .send(userActor)
                .message(message)
                .userId(to)
                .execute();
    }
}
