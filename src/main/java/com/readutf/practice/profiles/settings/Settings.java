package com.readutf.practice.profiles.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Settings {

    @Getter @Setter boolean scoreboard;
    @Getter @Setter boolean showSpectators;
    @Getter @Setter boolean receiveMessages;

}
