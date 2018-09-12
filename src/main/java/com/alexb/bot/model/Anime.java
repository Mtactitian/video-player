package com.alexb.bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anime {
    private String name;
    private Integer currentEpisodeNo;

    public void incrementEpisodeNo() {
        currentEpisodeNo++;
    }
}
