package com.jmeter.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmeter.pojo.API_Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GenerateReport {
    private static final String baselineLatest = "report/baseline_latest.json";
    private static String previousRunLatest = "report/previousRun_Recent.json";
    private static final String currentRun = "currentRun.json";
    private static String previousReportPath = "previousReport";
    private static String previousReportUpdated = "previousRun_Updated.json";

    public static void main(String[] args) throws IOException {
        System.out.println("System:" + System.getProperty("previousReport"));
        if (System.getProperty("previousReport").equalsIgnoreCase("true")) {
            previousRunLatest = previousReportPath + "/"+getPreviousFile(previousReportPath);
        }

        ReportCreator rc = new ReportCreator();

        //This generates api statistics for current run
        List<API_Response> currentRunStats = updateStatisticsRecords("statistics.json");
        sortCollection(currentRunStats);

        //This reads the baseline statistics for API's
        ObjectMapper mapper = new ObjectMapper();
        List<API_Response> baseStatsList = Arrays.asList(mapper.readValue(new File(GenerateReport.class.getClassLoader().getResource(baselineLatest).getPath()), API_Response[].class));
        Map<String, API_Response> baselineMap = convertListToMap(baseStatsList);

        //This reads the Previous build statistics for API's
        List<API_Response> previousrunStatsList = null;
        Map<String, API_Response> previousRunMap = null;
        if (System.getProperty("previousReport").equalsIgnoreCase("true")) {
            previousrunStatsList = updateStatisticsRecords(previousRunLatest);
            previousRunMap = convertListToMap(previousrunStatsList);
        } else{
            previousrunStatsList = Arrays.asList(mapper.readValue(new File(GenerateReport.class.getClassLoader().getResource(previousRunLatest).getPath()), API_Response[].class));
            previousRunMap = convertListToMap(previousrunStatsList);
        }

        double[] summary = getSummary(currentRunStats);
        String reportSummaryHtml = rc.generateSummary(summary);
        String reportStatsHtml = rc.generateStatistics(currentRunStats, previousRunMap, baselineMap);

        try {
            String completeReport = rc.generateCompleteReport(reportSummaryHtml, reportStatsHtml);
            rc.publishReport(completeReport, "summary.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPreviousFile(String previousReportPath) {
        String file = "";
        Path path = Paths.get(previousReportPath);
        File fileDir = new File(String.valueOf(path));
        if (fileDir.isDirectory()) {
            List<String> listFile = Arrays.asList(fileDir.list());
            final Pattern p = Pattern.compile("^\\d+");
            Comparator<String> c = new Comparator<String>() {
                @Override
                public int compare(String object1, String object2) {
                    Matcher m = p.matcher(object1);
                    Integer number1 = null;
                    if (!m.find()) {
                        return object1.compareTo(object2);
                    }
                    else {
                        Integer number2 = null;
                        number1 = Integer.parseInt(m.group());
                        m = p.matcher(object2);
                        if (!m.find()) {
                            return object1.compareTo(object2);
                        }
                        else {
                            number2 = Integer.parseInt(m.group());
                            int comparison = number1.compareTo(number2);
                            if (comparison != 0) {
                                return comparison;
                            }
                            else {
                                return object1.compareTo(object2);
                            }
                        }
                    }
                }
            };
            Collections.sort(listFile, c);
            System.out.println(listFile);
            file = (String) listFile.get(listFile.size()-1);
        } else {
            System.out.println(fileDir.getAbsolutePath() + " is not a directory");
        }

        return file;
    }

    private static double[] getSummary(List<API_Response> apis) {
        double[] summary = {0, 0, 0, 0};

        for (API_Response api : apis) {
            if (!api.getTransaction().equals("Total")) {
                summary[0] = summary[0] + api.getSampleCount();
                summary[1] = summary[1] + api.getErrorCount();
            }
        }

        summary[2] = Math.round(((summary[0] - summary[1]) / summary[0]) * 100 * 100) / 100.0;
        summary[3] = Math.round(((summary[1]) / summary[0]) * 100 * 100) / 100.0;

        return summary;
    }

    public static List<API_Response> updateStatisticsRecords(String fileName) throws IOException {
        String stringToBeMatched = new String(Files.readAllBytes(Paths.get(fileName)));
        String[] regex = {".*?\" : \\{", "^\\{", "}$"};
        String[] replaceWith = {"        {", "[", "]"};
        Pattern pattern = null;
        Matcher matcher = null;
        String content = null;

        for (int i = 0; i < 3; i++) {
            pattern = Pattern.compile(regex[i]);
            matcher = pattern.matcher(stringToBeMatched);
            content = matcher.replaceAll(replaceWith[i]);
            stringToBeMatched = content;
        }

        String path = new File(GenerateReport.class.getClassLoader().getResource("report").getFile()).getAbsolutePath();
        FileWriter file = new FileWriter(new File(path + "/" + currentRun));
        file.flush();
        file.append(content);
        file.close();

        ObjectMapper mapper = new ObjectMapper();
        List<API_Response> apis = Arrays.asList(mapper.readValue(new File(path + "/" + currentRun), API_Response[].class));
        return apis;
    }

    public static void sortCollection(List<API_Response> apis) {
        Collections.sort(apis, new Comparator() {
            public int compare(Object o1, Object o2) {
                API_Response a1 = (API_Response) o1;
                API_Response a2 = (API_Response) o2;
                return a1.getTransaction().compareTo(a2.getTransaction());
            }
        });
    }

    public static Map<String, API_Response> convertListToMap(List<API_Response> stats) {
        Map<String, API_Response> map = stats.stream()
                .collect(Collectors.toMap(API_Response::getTransaction, apiResponse -> apiResponse));
        return map;
    }
}