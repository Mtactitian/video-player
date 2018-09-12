package com.alexb.bot.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AnimeSubscription {

    private List<Anime> animes = new ArrayList<>();
    private Set<Integer> recipientsIds = new HashSet<>();

    @PostConstruct
    public void initialize() {
        try {
            File animesFile = new File(ClassLoader.getSystemResource("animes.json").toURI());
            File recipientsFile = new File(ClassLoader.getSystemResource("recipients.json").toURI());
            ObjectMapper objectMapper = new ObjectMapper();
            this.animes = objectMapper.readValue(animesFile, new TypeReference<List<Anime>>() {
            });
            this.recipientsIds = objectMapper.readValue(recipientsFile, new TypeReference<Set<Integer>>() {
            });
        } catch (URISyntaxException | IOException e) {
            log.error("Could not initialize anime subscription");
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            File animesFile = new File(ClassLoader.getSystemResource("animes.json").toURI());
            File recipientsFile = new File(ClassLoader.getSystemResource("recipients.json").toURI());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(animesFile, animes);
            objectMapper.writeValue(recipientsFile, recipientsIds);
        } catch (IOException | URISyntaxException ignored) {
        }
    }


}
