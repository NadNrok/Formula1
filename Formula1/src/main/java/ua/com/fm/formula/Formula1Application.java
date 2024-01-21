package ua.com.fm.formula;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

class LapRecord {
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

public class Formula1Application {

	public static void main(String[] args) {
		try {
			List<String> startLogLines = readLogFile("start.txt");
			List<String> endLogLines = readLogFile("end.txt");
			List<String> abbreviations = readLogFile("abbreviations.txt");

			List<LapRecord> lapRecords = parseLogData(startLogLines, endLogLines);

			Map<String, LapRecord> racerMap = lapRecords.stream()
					.collect(Collectors.toMap(LapRecord::getRacerAbbreviation, lapRecord -> lapRecord));

			setRacerInfo(abbreviations, racerMap);

			printTopRacers(lapRecords);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> readLogFile(String fileName) throws IOException {
		Path filePath = Paths.get(fileName);
		return Files.readAllLines(filePath);
	}

	private static List<LapRecord> parseLogData(List<String> startLogLines, List<String> endLogLines) {
		List<LapRecord> lapRecords = new ArrayList<>();

		for (int i = 0; i < startLogLines.size(); i++) {
			String startLine = startLogLines.get(i);
			String endLine = endLogLines.get(i);

			String racerAbbreviation = startLine.substring(0, 3);
			String lapTime = calculateLapTime(startLine, endLine);

			lapRecords.add(new LapRecord(racerAbbreviation, lapTime));
		}

		return lapRecords;
	}

	private static String calculateLapTime(String startLine, String endLine) {
		return endLine.substring(11);
	}

	private static void setRacerInfo(List<String> abbreviations, Map<String, LapRecord> racerMap) {
		for (String abbreviationLine : abbreviations) {
			String[] parts = abbreviationLine.split("_");
			String racerAbbreviation = parts[0];
			String racerName = parts[1];
			String team = parts[2];

			LapRecord lapRecord = racerMap.get(racerAbbreviation);
			if (lapRecord != null) {
				lapRecord.setRacerName(racerName);
				lapRecord.setTeam(team);
			}
		}
	}

	private static void printTopRacers(List<LapRecord> lapRecords) {
		lapRecords.sort(Comparator.comparing(LapRecord::getLapTime));

		int topCount = Math.min(15, lapRecords.size());
		for (int i = 0; i < topCount; i++) {
			LapRecord lapRecord = lapRecords.get(i);
			System.out.println((i + 1) + ". " + lapRecord);
		}

		System.out.println("------------------------------------------------------------------------");

		for (int i = topCount; i < lapRecords.size(); i++) {
			LapRecord lapRecord = lapRecords.get(i);
			System.out.println((i + 1) + ". " + lapRecord);
		}
	}
}