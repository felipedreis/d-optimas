package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.MemoryState;
import br.cefetmg.lsi.bimasco.persistence.dao.MemoryStateDAO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AgentMemoryProbabilitiesOverTime extends Extractor<MemoryState> {

    private MemoryStateDAO memoryStateDAO;

    private String agentName;

    public AgentMemoryProbabilitiesOverTime(String problem,
                                            MemoryStateDAO memoryStateDAO,
                                            String agentName) {
        super(problem);
        this.memoryStateDAO = memoryStateDAO;
        this.agentName = agentName;
    }

    @Override
    public List<MemoryState> getData() {
        List<MemoryState> agentMemories = memoryStateDAO.findByProblemAndAgent(problem, agentName).all();
        agentMemories = agentMemories.stream().filter(s -> MemoryState.MEMORY_STATUS_USED.equals(s.getMemoryStatus()))
                .sorted(Comparator.comparing(MemoryState::getTime))
                .collect(Collectors.toList());
        return agentMemories;
    }

    @Override
    public String formatDataToCsv(MemoryState memoryState) {
        StringBuilder builder = new StringBuilder();

        int lines = memoryState.getRegions().size();
        int i = 0;

        while (i < lines) {
            builder.append(memoryState.getTime())
                    .append(", ")
                    .append(memoryState.getRegions().get(i))
                    .append(", ")
                    .append(memoryState.getProbabilities().get(i));
            i++;

            if (i != lines)
                builder.append('\n');

        }
        return builder.toString();
    }

    @Override
    public String getFileName() {
        return agentName + "-memoriesOverTime";
    }
}
