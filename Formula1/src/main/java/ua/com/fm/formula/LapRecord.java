package ua.com.fm.formula;

public class LapRecord {
	private String racerAbbreviation;
	private String racerName;
	private String team;
	private String lapTime;

	public LapRecord(String racerAbbreviation, String lapTime) {
		this.racerAbbreviation = racerAbbreviation;
		this.lapTime = lapTime;
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

	public String getLapTime() {
		return lapTime;
	}

	@Override
	public String toString() {
		return racerName + " | " + team + " | " + lapTime;
	}
}