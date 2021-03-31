package com.jmeter.reports;

import com.jmeter.pojo.API_Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ReportCreator {
    private String totalBuildTime;
    private List<String[]> suiteDetails;
    private Map<String, String> suiteFailureDetails;
    private List<String[]> projectDetails;
    private Set<String> skippedTestDetails;

    public String generateSummary(double[] summary) {
        String tableData = "<table style=\"font-size: 12px;width: 100%;border-spacing: 2px;border-color:grey\">\r\n" +
                "<thead style=\"width: 100%;border-spacing: 2px;border-color:grey\">\r\n" +
                "<tr>";
        for (int i = 0; i < getHeaderForSummary().length; i++) {
            tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 8px;background-color: #508abb;color: #fff;\">" + getHeaderForSummary()[i] + "</th>";
        }
        tableData = tableData + "</tr>";

        tableData = tableData + "<tr style=\"width: 100%;border-bottom:1px solid #efefef;border-top:1px solid #ececec;background-color:#f4fbff;\">";
        for (double number : summary) {
            tableData = tableData + "<td style=\"border-collapse:seperate;text-align: center; padding: 8px\">" + Math.round(number * 100.0) / 100.0 + "</td>";
        }
        tableData = tableData + "</tr></thead><tbody style=\"font-size: 12px;\"></table>";
        return tableData;
    }

    public String generateStatistics(List<API_Response> currRunStats, Map<String, API_Response> previousRun, Map<String, API_Response> baseline) {
        String tableData = "<table style=\"font-size: 10px;width: 100%;border-spacing: 2px;border-color:grey\">\r\n" +
                "					<tr>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: #508abb;color: #fff;\">" + getHeaderForStatistics()[0] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: #508abb;color: #fff;\">" + getHeaderForStatistics()[1] + "</th>";
        tableData = tableData + "<th colspan=\"3\" style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: #508abb;color: #fff;\">" + getHeaderForStatistics()[2] + "</th>";
        tableData = tableData + "<th colspan=\"7\" style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: #508abb;color: #fff;\">" + getHeaderForStatistics()[3] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: #508abb;color: #fff;\">" + getHeaderForStatistics()[4] + "</th>";
        tableData = tableData + "<th colspan=\"2\" style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: #508abb;color: #fff;\">" + getHeaderForStatistics()[5] + "</th>";

        tableData = tableData + "</tr> <tr>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: #508abb;color: #fff;\"></th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: #508abb;color: #fff;\">" + getHeaderLabelsForStatistics()[0] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: darkslategray;color: #fff;\">" + getHeaderLabelsForStatistics()[1] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: darkslategray;color: #fff;\">" + getHeaderLabelsForStatistics()[2] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: darkslategray;color: #fff;\">" + getHeaderLabelsForStatistics()[3] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: lightseagreen;color: #fff;\">" + getHeaderLabelsForStatistics()[4] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: lightseagreen;color: #fff;\">" + getHeaderLabelsForStatistics()[5] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: lightseagreen;color: #fff;\">" + getHeaderLabelsForStatistics()[6] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: lightseagreen;color: #fff;\">" + getHeaderLabelsForStatistics()[7] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: lightseagreen;color: #fff;\">" + getHeaderLabelsForStatistics()[8] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: lightseagreen;color: #fff;\">" + getHeaderLabelsForStatistics()[9] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: lightseagreen;color: #fff;\">" + getHeaderLabelsForStatistics()[10] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: cornflowerblue;color: #fff;\">" + getHeaderLabelsForStatistics()[11] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: cornflowerblue;color: #fff;\">" + getHeaderLabelsForStatistics()[12] + "</th>";
        tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 4px;background-color: cornflowerblue;color: #fff;\">" + getHeaderLabelsForStatistics()[13] + "</th>";
        tableData = tableData + "</tr>";

        int serialNumber = 1;
        for (int i = 0; i < currRunStats.size(); i++) {
            if (!currRunStats.get(i).getTransaction().equals("Total")) {
                tableData = tableData + "<tr style=\"width: 100%;border-bottom:1px solid #efefef;border-top:1px solid #ececec;background-color:#f4fbff;\">";
                tableData = tableData + "<td style=\"border-collapse:collapse;text-align: left; padding: 4px\">" + serialNumber + "</td>";
                tableData = tableData + "<td style=\"border-collapse:collapse;text-align: left; padding: 4px\">" + currRunStats.get(i).getTransaction() + "</td>";
                tableData = tableData + "<td style=\"border-collapse:collapse;text-align: center; padding: 4px\">" + currRunStats.get(i).getSampleCount() + "</td>";
                if (currRunStats.get(i).getErrorCount() > 0) {
                    tableData = tableData + "<td style=\"border-collapse:collapse;text-align: center; padding: 4px\"> <b>" + currRunStats.get(i).getErrorCount() + " </b> </td>";
                } else {
                    tableData = tableData + "<td style=\"border-collapse:collapse;text-align: center; padding: 4px\">" + currRunStats.get(i).getErrorCount() + "</td>";
                }

                if (currRunStats.get(i).getErrorPct() > 0) {
                    tableData = tableData + "<td style=\"border-collapse:collapse;text-align: center; padding: 4px\"> <b>" + currRunStats.get(i).getErrorPct() + "</b> </td>";
                } else {
                    tableData = tableData + "<td style=\"border-collapse:collapse;text-align: center; padding: 4px\">" + currRunStats.get(i).getErrorPct() + "</td>";
                }

                tableData = tableData + "<td style=\"border-collapse:collapse;padding: 4px\">" + getDisplayField(currRunStats, previousRun, baseline, i, "mean") + "</td>";
                tableData = tableData + "<td style=\"border-collapse:collapse;padding: 4px\">" + getDisplayField(currRunStats, previousRun, baseline, i, "median") + "</td>";
                tableData = tableData + "<td style=\"border-collapse:collapse;padding: 4px\">" + getDisplayField(currRunStats, previousRun, baseline, i, "min") + "</td>";
                tableData = tableData + "<td style=\"border-collapse:collapse;padding: 4px\">" + getDisplayField(currRunStats, previousRun, baseline, i, "max") + "</td>";
                tableData = tableData + "<td style=\"border-collapse:collapse;padding: 4px\">" + getDisplayField(currRunStats, previousRun, baseline, i, "90th") + "</td>";
                tableData = tableData + "<td style=\"border-collapse:collapse;padding: 4px\">" + getDisplayField(currRunStats, previousRun, baseline, i, "95th") + "</td>";
                tableData = tableData + "<td style=\"border-collapse:collapse;padding: 4px\">" + getDisplayField(currRunStats, previousRun, baseline, i, "99th") + "</td>";
                tableData = tableData + "<td style=\"text-align: center;border-collapse:collapse;padding: 4px\">" + Math.round(currRunStats.get(i).getThroughput()) + "</td>";
                tableData = tableData + "<td style=\"text-align: center;border-collapse:collapse;padding: 4px\">" + Math.round(currRunStats.get(i).getReceivedKBytesPerSec()) + "</td>";
                tableData = tableData + "<td style=\"text-align: center;border-collapse:collapse;padding: 4px\">" + Math.round(currRunStats.get(i).getSentKBytesPerSec()) + "</td>";
                tableData = tableData + "</tr>";

                serialNumber++;
            }
        }
        tableData = tableData + "</thead><tbody style=\"font-size: 14px;\"></table>";
        return tableData;
    }

    private String getDisplayField(List<API_Response> currRunStats, Map<String, API_Response> previousRun, Map<String, API_Response> baseline, int i, String metric) {
        String displayVar = "error";
        double current;
        double previousDelta;
        double baselineDelta;

        switch (metric) {
            case "mean":
                //If previous run and baslines have current API records, then we can compare, else display as 'NA' for previous and baseline
                current = Math.round(currRunStats.get(i).getMeanResTime());
                if (baseline.containsKey(currRunStats.get(i).getTransaction()) && previousRun.containsKey(currRunStats.get(i).getTransaction())) {
                    previousDelta = (current - Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getMeanResTime()))/Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getMeanResTime()) * 100;
                    baselineDelta = (current - Math.round(baseline.get(currRunStats.get(i).getTransaction()).getMeanResTime()))/Math.round(baseline.get(currRunStats.get(i).getTransaction()).getMeanResTime()) * 100;
                    //to handle infinity case
                    if(Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getMeanResTime()) == 0 ||
                            Math.round(baseline.get(currRunStats.get(i).getTransaction()).getMeanResTime()) == 0){
                        displayVar = current + flagIfAboveThreshold(0, 0);
                    }else{
                        displayVar = current + flagIfAboveThreshold(previousDelta, baselineDelta);
                    }
                } else {
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(NA)<i style=\"color:orange\">[NA]</i></span>";
                }
                break;
            case "median":
                current = Math.round(currRunStats.get(i).getMedianResTime());
                if (baseline.containsKey(currRunStats.get(i).getTransaction()) && previousRun.containsKey(currRunStats.get(i).getTransaction())) {
                    previousDelta = (current - Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getMedianResTime())) / Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getMedianResTime()) * 100;
                    baselineDelta = (current - Math.round(baseline.get(currRunStats.get(i).getTransaction()).getMedianResTime())) / Math.round(baseline.get(currRunStats.get(i).getTransaction()).getMedianResTime()) * 100;
                    //to handle infinity case
                    if(Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getMedianResTime()) == 0 ||
                            Math.round(baseline.get(currRunStats.get(i).getTransaction()).getMedianResTime()) == 0){
                        displayVar = current + flagIfAboveThreshold(0, 0);
                    }else{
                        displayVar = current + flagIfAboveThreshold(previousDelta, baselineDelta);
                    }
                } else {
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(NA)<i style=\"color:orange\">[NA]</i></span>";
                }
                break;
            case "min":
                current = Math.round(currRunStats.get(i).getMinResTime());
                if (baseline.containsKey(currRunStats.get(i).getTransaction()) && previousRun.containsKey(currRunStats.get(i).getTransaction())) {
                    previousDelta = current - Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getMinResTime());
                    baselineDelta = current - Math.round(baseline.get(currRunStats.get(i).getTransaction()).getMinResTime());
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(" + previousDelta + ")<i style=\"color:orange\">[" + baselineDelta + "]</i></span>";
                } else {
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(NA)<i style=\"color:orange\">[NA]</i></span>";
                }
                break;
            case "max":
                current = Math.round(currRunStats.get(i).getMaxResTime());
                if (baseline.containsKey(currRunStats.get(i).getTransaction()) && previousRun.containsKey(currRunStats.get(i).getTransaction())) {
                    previousDelta = current - Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getMaxResTime());
                    baselineDelta = current - Math.round(baseline.get(currRunStats.get(i).getTransaction()).getMaxResTime());
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(" + previousDelta + ")<i style=\"color:orange\">[" + baselineDelta + "]</i></span>";
                } else {
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(NA)<i style=\"color:orange\">[NA]</i></span>";
                }
                break;
            case "90th":
                current = Math.round(currRunStats.get(i).getPct1ResTime());
                if (baseline.containsKey(currRunStats.get(i).getTransaction()) && previousRun.containsKey(currRunStats.get(i).getTransaction())) {
                    previousDelta = (current - Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct1ResTime()))/Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct1ResTime()) * 100;
                    baselineDelta = (current - Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct1ResTime()))/Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct1ResTime()) * 100;
                    //to handle infinity case
                    if(Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct1ResTime()) == 0 ||
                            Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct1ResTime()) == 0){
                        displayVar = current + flagIfAboveThreshold(0, 0);
                    }else{
                        displayVar = current + flagIfAboveThreshold(previousDelta, baselineDelta);
                    }
                } else {
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(NA)<i style=\"color:orange\">[NA]</i></span>";
                }
                break;
            case "95th":
                current = Math.round(currRunStats.get(i).getPct2ResTime());
                if (baseline.containsKey(currRunStats.get(i).getTransaction()) && previousRun.containsKey(currRunStats.get(i).getTransaction())) {
                    previousDelta = (current - Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct2ResTime()))/Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct2ResTime()) * 100;
                    baselineDelta = (current - Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct2ResTime()))/Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct2ResTime()) * 100;
                    //to handle infinity case
                    if(Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct2ResTime()) == 0 ||
                            Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct2ResTime()) == 0){
                        displayVar = current + flagIfAboveThreshold(0, 0);
                    }else{
                        displayVar = current + flagIfAboveThreshold(previousDelta, baselineDelta);
                    }
                } else {
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(NA)<i style=\"color:orange\">[NA]</i></span>";
                }
                break;
            case "99th":
                current = Math.round(currRunStats.get(i).getPct3ResTime());
                if (baseline.containsKey(currRunStats.get(i).getTransaction()) && previousRun.containsKey(currRunStats.get(i).getTransaction())) {
                    previousDelta = (current - Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct3ResTime()))/Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct3ResTime()) * 100;
                    baselineDelta = (current - Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct3ResTime()))/Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct3ResTime()) * 100;
                    //to handle infinity case
                    if(Math.round(previousRun.get(currRunStats.get(i).getTransaction()).getPct3ResTime()) == 0 ||
                            Math.round(baseline.get(currRunStats.get(i).getTransaction()).getPct3ResTime()) == 0){
                        displayVar = current + flagIfAboveThreshold(0, 0);
                    }else{
                        displayVar = current + flagIfAboveThreshold(previousDelta, baselineDelta);
                    }
                } else {
                    displayVar = current + "<span style=\"float:right;font-size:75%;color:blue;font-family: monospace\">(NA)<i style=\"color:orange\">[NA]</i></span>";
                }
                break;
            default:
                throw new IllegalStateException("ERROR: performance metric '" + metric + "' not supported" + metric);
        }

        return displayVar;
    }

    private String flagIfAboveThreshold(double previousDelta, double baselineDelta) {
        String result = "";
        previousDelta = Math.round(previousDelta);
        baselineDelta = Math.round(baselineDelta);
        if(previousDelta > 10 || baselineDelta > 10){
            result = "<span style=\"float:right;font-size:75%;color:red;font-family:monospace\"> <b>(" + previousDelta + "%)</b><b style=\"color:red\">[" + baselineDelta + "%]</b></span>";
        } else{
            result = "<span style=\"float:right;font-size:75%;color:blue;font-family:monospace\"> (" + previousDelta + "%)<i style=\"color:orange\">[" + baselineDelta + "%]</i></span>";
        }
        return result;
    }

    public String generateCompleteReport(String apiSummary, String apiStatistics) throws IOException {
        //Scanner scanner = new Scanner(Paths.get("src/test/resources/report/build-summary.txt"),
        Scanner scanner = new Scanner(Paths.get(ReportCreator.class.getClassLoader().getResource("report/build-summary.txt").getPath()), StandardCharsets.UTF_8.name());
        String summary = scanner.useDelimiter("\\A").next();
        scanner.close();
        summary = summary.replace("{{SUMMARY}}", apiSummary);
        summary = summary.replace("{{STATISTICS}}", apiStatistics);
        return summary;
    }

    public void publishReport(String reportSummary, String fileDetails) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(fileDetails, "UTF-8");
        writer.println(reportSummary);
        writer.close();
    }

    private String[] getHeaderForSummary() {
        String[] headValues = new String[4];
        headValues[0] = "#Samples";
        headValues[1] = "#Failures";
        headValues[2] = "Success Rate(%)";
        headValues[3] = "Failure Rate(%)";
        return headValues;
    }

    private String[] getHeaderForStatistics() {
        String[] headValues = new String[6];
        headValues[0] = "S.No";
        headValues[1] = "Requests";
        headValues[2] = "Executions";
        headValues[3] = "Response Times (ms) <br><span style=\"color:black;font-family: monospace\">current_run</span> <span style=\"color:blue;font-family: monospace\">(previous_run delta)</span> <span style=\"color:orange;font-family: monospace\">[baseline_run delta]</span>";
        headValues[4] = "Throughput";
        headValues[5] = "Network (KB/sec)";
        return headValues;
    }

    private String[] getHeaderLabelsForStatistics() {
        String[] headValues = new String[14];
        headValues[0] = "Label";
        headValues[1] = "#Samples";
        headValues[2] = "#Error";
        headValues[3] = "Error(%)";
        headValues[4] = "Average";
        headValues[5] = "Median";
        headValues[6] = "Min Time(ms)";
        headValues[7] = "Max Time(ms)";
        headValues[8] = "90th percentile";
        headValues[9] = "95th percentile";
        headValues[10] = "99th percentile";
        headValues[11] = "Transactions/s";
        headValues[12] = "Received";
        headValues[13] = "Sent";

        return headValues;
    }
}
