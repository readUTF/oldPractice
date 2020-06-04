package com.readutf.practice.scoreboard;

import com.readutf.practice.Practice;
import com.readutf.practice.match.Match;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.queue.Queue;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.party.Party;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.settings.Setting;
import com.readutf.practice.settings.SettingsManager;
import com.readutf.practice.tournament.Tournament;
import com.readutf.practice.tournament.TournamentManager;
import com.readutf.practice.utils.Cooldown;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.aether.scoreboard.Board;
import com.readutf.uLib.aether.scoreboard.BoardAdapter;
import com.readutf.uLib.aether.scoreboard.cooldown.BoardCooldown;
import com.readutf.uLib.libraries.ColorUtil;
import com.readutf.uLib.libraries.Players;
import com.readutf.uLib.libraries.duration.DurationFormatter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import javax.imageio.ImageTranscoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ScoreboardManager implements BoardAdapter {
    @Override
    public String getTitle(Player player) {
        return "&4Railed &7(US)";
    }

    @Override
    public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> set) {
        Profile profile = Profile.getUser(player.getUniqueId());
        if(profile == null) {
            return null;
        }
        if(!SettingsManager.get().getSettingValue(Setting.SHOW_SCOREBOARD, profile)) {
            return null;
        }


        ArrayList<String> lines = new ArrayList<>();

        if(profile.getGameState() == GameState.LOBBY) {
            Tournament tournament = TournamentManager.get().getActiveTournament();
            if(tournament != null) {
                lines.add(ColorUtil.color("&7&m--------------------"));
                lines.add(ColorUtil.color("&4&lTournament"));
                lines.add(ColorUtil.color("  &cCompetitors &7(" + tournament.getCompetitors().size()  + "/" + tournament.getMaxSize() + ")"));
                lines.add(ColorUtil.color("  &cRound: " + (tournament.getRound() == 0 ? "&aWarmup" : tournament.getRound() + "")));
            }
            lines.add(ColorUtil.color("&7&m--------------------"));
            lines.add("&cOnline: &f" + Players.getOnlinePlayers().size());
            lines.add("&cFighting: &f" + Practice.get().getIngame());
            lines.add("&cQueued: &f" + MatchManager.get().getQueued());
            lines.add("");
            lines.add("&cCoins: &f" + profile.getCoins());
            lines.add("");
            lines.add("&7railed.rip");
            lines.add(ColorUtil.color("&7&m--------------------"));
        } else if(profile.getGameState() == GameState.FIGHTING) {
            Match match = profile.getActiveMatch();

            lines.add(ColorUtil.color("&7&m--------------------"));
            lines.add(ColorUtil.color("&cOpponent: &f" + match.getOponent(player).get(0).getName()));
            if(!match.inWarmup) {


                lines.add(ColorUtil.color("&cDuration: &f" + format(match.getDuration())));
            }
            lines.add(ColorUtil.color("&cLadder: &a" + match.getKit().getName()));
            if(Cooldown.isInCooldown(player.getUniqueId(), "enderpearl")) {
                lines.add(ColorUtil.color("&cEnderpearl: &f" + DurationFormatter.getRemaining(Cooldown.getTimeLeft(player.getUniqueId(), "enderpearl"), true)));
            }
            lines.add(ColorUtil.color("&7&m--------------------"));
        } else if(profile.getGameState() == GameState.QUEUEING) {
            Queue queue = profile.getQueue();
            lines.add(ColorUtil.color("&7&m--------------------"));
            lines.add(ColorUtil.color("&4&lQueue"));
            lines.add(ColorUtil.color(" &cKit: &f" + WordUtils.capitalize(queue.getKit().getName())));
            lines.add(ColorUtil.color(" &cLadder: &f" + WordUtils.capitalize(queue.getQueueType().name().toLowerCase())));
            lines.add(ColorUtil.color(" &cTime: &f" + DurationFormatter.getRemaining(System.currentTimeMillis() - profile.getQueue().getStartTimes().get(player), true)));
            if(queue.getQueueType() == QueueType.RANKED) {
                lines.add(ColorUtil.color(" &cElo: &f" + profile.getElo().get(queue.getKit().getName())));
            }
            lines.add(ColorUtil.color("&7&m--------------------"));
        } else if(profile.getGameState() == GameState.SPECTATING) {
            Match spectating = profile.getSpectatingMatch();
            lines.add(ColorUtil.color("&7&m--------------------"));
            lines.add(ColorUtil.color("&4&lSpectating"));
            lines.add(SpigotUtils.color(" &cDuration: &f" + DurationFormatter.getRemaining(spectating.getDuration(), true)));
            lines.add(SpigotUtils.color(" &cKit: &f" + WordUtils.capitalize(spectating.getKit().getName().toLowerCase())));
            lines.add(ColorUtil.color("&7&m--------------------"));
        } else if(profile.getGameState() == GameState.PARTY) {
            Party party = profile.getParty();
            lines.add(ColorUtil.color("&7&m--------------------"));
            lines.add("&4&lParty");
            lines.add(" &cLeader: &f" + party.getLeader().getName());
            lines.add(" &cMembers: &f" + (party.getMembers().size() + 1));
            lines.add(" &cInvite-Only: &f" + party.isInviteOnly());
            lines.add(ColorUtil.color("&7&m--------------------"));
        } else if(profile.getGameState() == GameState.STAFF) {

            lines.add(ColorUtil.color("&7&m--------------------"));
            lines.add(ColorUtil.color("&cTPS: &f" + colourTps(Bukkit.spigot().getTPS()[0]) +""));
            lines.add(ColorUtil.color("&cVisibile: &fFalse"));
            lines.add(ColorUtil.color("&7&m--------------------"));
            lines.add("&cOnline: &f" + Players.getOnlinePlayers().size());
            lines.add("&cIngame: &f" + Practice.get().getIngame());
            lines.add("&cIn Queue: &f" + MatchManager.get().getQueued());
            lines.add(ColorUtil.color("&7&m--------------------"));
        }



        return lines;
    }

    public long removeMillis(long value) {
        int x = (int) value / 1000;
        return x * 1000;
    }

    public String format(long millis) {

        int i = (int) millis / 1000;




        int seconds = i % 60;
        int minutes = (i - seconds) / 60;

        String secondsFormatted;
        if(seconds < 10) {
            secondsFormatted = "0" + seconds;
        } else {
            secondsFormatted = "" + seconds;
        }

        String minutesFormatted;
        if(minutes < 10) {
            minutesFormatted = "0" + minutes;
        } else {
            minutesFormatted = "" + minutes;
        }

        return minutesFormatted + ":" + secondsFormatted;



    }

    public String colourTps(double d) {
        if (d > 15.0D)
            return SpigotUtils.color("&a" + Math.round(d));
        if (d > 10.0D) {
            return SpigotUtils.color("&e" + Math.round(d));
        }
        return SpigotUtils.color("&c" + Math.round(d));
    }

    public String trim(String s) {
        return s.substring(0, Math.min(s.length(), 16));
    }

}
