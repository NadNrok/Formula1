package ua.com.fm.formula;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Formula1Application {

    private static final String START_LOG_FILE = "start.txt";
    private static final String END_LOG_FILE = "end.txt";
    private static final String ABBREVIATIONS_FILE = "abbreviations.txt";

    public static void main(String[] args) {
        try {
            List<String> startLogLines = readLogFile(START_LOG_FILE);
            List<String> endLogLines = readLogFile(END_LOG_FILE);
            List<String> abbreviations = readLogFile(ABBREVIATIONS_FILE);

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
        try (InputStream inputStream = Formula1Application.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            return reader.lines().collect(Collectors.toList());
        }
    }

    private static List<LapRecord> parseLogData(List<String> startLogLines, List<String> endLogLines) {
        List<LapRecord> lapRecords = new ArrayList<>();

        for (int i = 0; i < startLogLines.size(); i++) {
            String startLine = startLogLines.get(i);
            String endLine = endLogLines.get(i);

            String racerAbbreviation = startLine.substring(0, 3);
            Duration lapDuration = calculateLapTime(startLine, endLine);

            lapRecords.add(new LapRecord(racerAbbreviation, lapDuration));
        }

        return lapRecords;
    }

    private static Duration calculateLapTime(String startLine, String endLine) {
        LocalTime startTime = parseTime(startLine);
        LocalTime endTime = parseTime(endLine);

        return Duration.between(startTime, endTime).abs();
    }

    private static LocalTime parseTime(String line) {
        String timeString = line.substring(line.indexOf("_") + 1);
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
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
        lapRecords.sort(Comparator.comparing(LapRecord::getLapDuration));

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