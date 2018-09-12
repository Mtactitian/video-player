package com.alexb.bot.routes.anime;

import com.alexb.bot.processors.GoGoAnimeUpdatesProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UpdatesScanRoute extends RouteBuilder {

    private final GoGoAnimeUpdatesProcessor animeUpdatesProcessor;

    @Override
    public void configure() {
        from("timer://sender?fixedRate=true&period=10s")
                .log(LoggingLevel.INFO, "Checking for new anime updates...")
                .process(animeUpdatesProcessor)
                .filter(ex -> Objects.nonNull(ex.getIn().getBody(String.class)))
                .to("bean:vkAnimeNotifier?method=send");
    }
}
