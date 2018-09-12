package com.alexb.bot.processors;


import com.alexb.bot.model.Anime;
import com.alexb.bot.model.AnimeSubscription;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoGoAnimeUpdatesProcessor implements Processor {

    private final AnimeSubscription animeSubscription;

    @Override
    public void process(Exchange exchange) throws IOException {
        Map<Anime, String> animeUpdates = new HashMap<>();

        for (Anime anime : animeSubscription.getAnimes()) {
            Document indexPage = Jsoup.connect("https://www1.gogoanime.sh/").get();
            Element animeElement = indexPage.select(String.format("a[title=%s]", anime.getName())).first();

            if (animeElement != null) {
                String href = animeElement.attr("href");
                Integer episodeNo = Integer.valueOf(href.substring(href.lastIndexOf("episode-")).replaceAll("episode-", ""));

                if (!episodeNo.equals(anime.getCurrentEpisodeNo())) {
                    anime.incrementEpisodeNo();
                    animeUpdates.put(anime, "https://www1.gogoanime.sh" + href);
                }
            }
        }

        if (!animeUpdates.isEmpty()) {
            exchange.getIn().setBody(formatBody(animeUpdates));
        }
    }

    private String formatBody(Map<Anime, String> map) {
        String message = "New Episodes: \n\n";

        for (Map.Entry<Anime, String> entry : map.entrySet()) {
            Anime anime = entry.getKey();
            message = message.concat(
                    anime.getName() + "\n"
                            + "Episode No: " + anime.getCurrentEpisodeNo() + "\n"
                            + "Watch url: " + entry.getValue()
            );
        }

        message = message.concat("\n\nIf you want to unsubscribe from new anime updates, please type \"Unsubscribe me\"."
                + "\n If you want to subscribe again, please type \"Subscribe me\" ");

        return message;
    }
}
