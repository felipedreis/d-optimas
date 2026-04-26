package br.cefetmg.lsi.bimasco.api;

import java.util.List;
import java.util.stream.Collectors;

public class TestConfigHelper {
    public static String buildDefaultConfig() {
        return buildSimulationConfig("Test", 60, 1, "results",
                buildProblemConfig("Function", "Function", false,
                        "br.cefetmg.lsi.bimasco.core.problems.FunctionProblem", "Function",
                        List.of(
                                List.of("Michalewicz"), List.of(2), List.of(0.1),
                                List.of(0.000001, 0.000001), List.of(0.0, 3.141592), List.of(0.0, 3.141592)
                        )),
                buildRegionConfig(10, 1, 10),
                List.of());
    }

    public static String buildSimulationConfig(String name, int executionTime, int nodes, String extractPath,
                                             String problem, String region, List<String> agents) {
        return "simulation: {\n" +
                "  name: \"" + name + "\",\n" +
                "  hasCooperation: false,\n" +
                "  executionTime: " + executionTime + ",\n" +
                "  nodes: " + nodes + ",\n" +
                "  extractPath: \"" + extractPath + "\",\n" +
                "  problem: " + problem + ",\n" +
                "  region: " + region + ",\n" +
                "  agents: [" + String.join(", ", agents) + "]\n" +
                "}";
    }

    public static String buildProblemConfig(String name, String type, boolean isMax, String classPath,
                                          String solutionAnalyserName, List<List<?>> problemData) {
        String data = problemData.stream()
                .map(list -> "[" + list.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]")
                .collect(Collectors.joining(", "));

        return "{\n" +
                "    name: \"" + name + "\",\n" +
                "    type: \"" + type + "\",\n" +
                "    isMax: " + isMax + ",\n" +
                "    classPath: \"" + classPath + "\",\n" +
                "    solutionAnalyserName: \"" + solutionAnalyserName + "\",\n" +
                "    problemData: [" + data + "]\n" +
                "  }";
    }

    public static String buildRegionConfig(int minSolutionsToSplit, int minRegions, int maxRegions) {
        return "{\n" +
                "    minSolutionsToSplit: " + minSolutionsToSplit + ",\n" +
                "    minRegions: " + minRegions + ",\n" +
                "    maxRegions: " + maxRegions + "\n" +
                "  }";
    }
}
