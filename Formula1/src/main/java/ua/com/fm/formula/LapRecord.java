package ua.com.fm.formula;

import java.time.Duration;

public class LapRecord {
    private String racerAbbreviation;
    private String racerName;
    private String team;
    private Duration lapDuration;

    public LapRecord(String racerAbbreviation, Duration lapDuration) {
        this.racerAbbreviation = racerAbbreviation;
        this.lapDuration = lapDuration;
    }

    public String getRacerAbbreviation() {
        return racerAbbreviation;
    }

    public String getRacerName() {
        return racerName;
    }

    public void setRacerName(String racerName) {
        this.racerName = racerName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Duration getLapDuration() {
        return lapDuration;
    }

    @Override
    public String toString() {
        long minutes = lapDuration.toMinutes();
        long seconds = lapDuration.minusMinutes(minutes).getSeconds();
        long millis = lapDuration.minusMinutes(minutes).minusSeconds(seconds).toMillis();

        return racerName + " | " + team + " | " + String.format("%d:%02d.%03d", minutes, seconds, millis);
    }
}