package com.alexb.bot.routes.anime;

import com.alexb.bot.processors.VkSubscriptionProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VkSubscriptionListenerRoute extends RouteBuilder {

    private final VkSubscriptionProcessor vkSubscriptionProcessor;

    @Override
    public void configure() {
        from("timer://sender?fixedRate=true&period=500")
                .process(vkSubscriptionProcessor);
    }
}
