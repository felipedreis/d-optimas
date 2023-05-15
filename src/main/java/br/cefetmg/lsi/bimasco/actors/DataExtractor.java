package br.cefetmg.lsi.bimasco.actors;

import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.persistence.*;
import br.cefetmg.lsi.bimasco.persistence.dao.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;
/**
 * Data extractor actor based on asynchronous
 */
public class DataExtractor  {

    private static final Logger logger = LoggerFactory.getLogger(DataExtractor.class);

    private GlobalStateDAO globalStateDAO;
    private RegionStateDAO regionStateDAO;
    private SolutionStateDAO solutionStateDAO;
    private MessageStateDAO messageStateDAO;
    private AgentStateDAO agentStateDAO;

    private String path;

    private SolutionAnalyser analyser;

    private Map<String, Supplier<Collection<GlobalState>>> globalStateSuppliers = Map.of(
            "regionsOverTime", this::globalStates,
            "globalStatisticsOverTime", this::globalStates
    );

    private Map<String, Function<GlobalState, String>> globalStateFormatters = Map.of(
            "regionsOverTime", this::globalStateRegionsOverTime,
            "globalStatisticsOverTime", this::globalStatisticsOverTime
    );

    private Map<String, Supplier<Collection<RegionState>>> regionStateSuppliers = new HashMap<>();

    private Map<String, Function<RegionState, String>> regionStateFormatters = new HashMap<>();

    private Map<String, Supplier<Collection<SolutionState>>> solutionStateSuppliers = new HashMap<>();

    private Map<String, Function<SolutionState, String>> solutionStateFormatters = new HashMap<>();

    private Map<String, Supplier<Collection<MessageState>>> messageStateSuppliers = new HashMap<>();

    private Map<String, Function<MessageState, String>> messageStateFormatters = new HashMap<>();

    private Map<String, Supplier<Collection<Map.Entry<Long, List<SolutionState>>>>> regionSolutionsSuppliers = new HashMap<>();

    private Map<String, Function<Map.Entry<Long, List<SolutionState>>,String>> regionSolutionsFormatters = new HashMap<>();

    private Map<String, Supplier<Collection<AgentState>>> agentStateSuppliers = new HashMap<>();

    private Map<String, Function<AgentState, String>> agentStateFormatters = new HashMap<>();

    public DataExtractor() {
        DOptimasMapperBuilder builder = new DOptimasMapperBuilder(DatabaseHelper.getCqlSession());
        DOptimasMapper mapper = builder.build();

        globalStateDAO = mapper.globalStateDAO();
        regionStateDAO = mapper.regionStateDAO();
        solutionStateDAO = mapper.solutionStateDAO();
        messageStateDAO = mapper.messageStateDAO();
        agentStateDAO = mapper.agentStateDAO();

        List<RegionState> regionStates = regionStateDAO.findAll().all();
        List<String> regionNames = regionStates.stream().map(RegionState::getName)
                .distinct()
                .collect(Collectors.toList());

        for (String name : regionNames) {
            regionStateSuppliers.put(name + "-regionStatisticsOverTime", regionStateSupplier(name));
            regionStateFormatters.put(name + "-regionStatisticsOverTime", this::regionStatisticsOverTime);

            solutionStateSuppliers.put(name + "-bestSolutionOverTime", regionBestSolutionSupplier(name));
            solutionStateFormatters.put(name + "-bestSolutionOverTime", this::bestSolutionOverTime);


            solutionStateSuppliers.put(name + "-solutionsOverTime", regionSolutionOverTime(name));
            solutionStateFormatters.put(name + "-solutionsOverTime", this::regionSolutionOverTime);

            regionSolutionsSuppliers.put(name + "-regionSolutionValues", regionSolutionsValuesOverTime(name));
            regionSolutionsFormatters.put(name + "-regionSolutionValues", this::regionSolutionsValuesOverTime);
        }

        List<AgentState> agentStates = agentStateDAO.findAll().all();
        Map<String, String> agentNames = agentStates.stream()
                .collect(Collectors.toMap(AgentState::getPersistentId, AgentState::getAlgorithmName, (a,b) -> a));

        for (Map.Entry<String, String> agent : agentNames.entrySet()) {
            agentStateSuppliers.put(agent.getKey() + "-" + agent.getValue() + "-statesOverTime",
                    agentStatesOverTime(agent.getKey()));
            agentStateFormatters.put(agent.getKey() + "-" + agent.getValue() + "-statesOverTime",
                    this::agentStatesOverTime);

            solutionStateSuppliers.put(agent.getKey() + "-" + agent.getValue() + "-solutionOverTime",
                    agentSolutionOverTime(agent.getKey()));
            solutionStateFormatters.put(agent.getKey() + "-" + agent.getValue() + "-solutionOverTime",
                    this::bestSolutionOverTime);

        }

        solutionStateSuppliers.put("globalBestSolutionOverTime", this::bestSolutionOverTime);
        solutionStateFormatters.put("globalBestSolutionOverTime", this::bestSolutionOverTime);

        messageStateSuppliers.put("messagesOverTime", this::messageStates);
        messageStateFormatters.put("messagesOverTime", this:: messageStates);

        messageStateSuppliers.put("mergeSplitOverTime", this::mergeSplit);
        messageStateFormatters.put("mergeSplitOverTime", this:: messageStates);
    }

    Supplier<Collection<RegionState>> regionStateSupplier(String regionName) {
        return () -> {
            List<RegionState> states = regionStateDAO.findByName(regionName).all();
            states.sort(Comparator.comparing(RegionState::getTime));
            return  states;
        };
    }

    Supplier<Collection<SolutionState>> regionBestSolutionSupplier(String regionName) {
        List<RegionState> regionStates = regionStateDAO.findByName(regionName).all();

        Supplier<Collection<SolutionState>> supplier;

        if (regionStates.isEmpty()) {
            supplier = Collections::emptyList;
        } else
            supplier = () -> {
            List<SolutionState> states = regionStates.stream()
                    .map(regionState -> solutionStateDAO.findById(regionState.getBestSolution(), regionName))
                    .sorted(Comparator.comparing(SolutionState::getTime))

                    .collect(Collectors.toList());
            return states;
        };

        return supplier;
    }

    Supplier<Collection<SolutionState>> regionSolutionOverTime(String regionName) {
        return () -> {
            List<SolutionState> solutionStates = solutionStateDAO.findByRegion(regionName).all();
            solutionStates.sort(Comparator.comparing(SolutionState::getTime));
            return solutionStates;
        };
    }

    Supplier<Collection<SolutionState>> agentSolutionOverTime(String agentName) {
        List<AgentState> agentStates = agentStateDAO.findByPersistentId(agentName).all();
        return () -> {
            List<UUID> ids = agentStates.stream().map(AgentState::getProducedSolution)
                    .collect(Collectors.toList());
            List<SolutionState> solutionStates = solutionStateDAO.findByIds(ids).all();
            solutionStates.sort(Comparator.comparing(SolutionState::getTime));
            return solutionStates;
        };
    }

    List<SolutionState> bestSolutionOverTime() {
        List<SolutionState> solutionStates = solutionStateDAO.findAll().all();
        Map<Long, Optional<SolutionState>> filtered = solutionStates.parallelStream()
                .collect(Collectors.groupingBy(SolutionState::getTime,
                        Collectors.minBy(Comparator.comparing(SolutionState::getFunctionValue))));

                return filtered.values().stream()
                    .map(solutionState -> solutionState.orElse(null))
                        .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(SolutionState::getTime))
                    .collect(Collectors.toList());
    }

    List<GlobalState> globalStates() {
        List<GlobalState> globalStates = globalStateDAO.findAll().all();
        globalStates.sort(Comparator.comparing(GlobalState::getTime));
        return globalStates;
    }

    List<MessageState> messageStates() {
        List<MessageState> messages = messageStateDAO.findAll().all();
        messages.sort(Comparator.comparing(MessageState::getTime));
        return messages;
    }

    List<MessageState> mergeSplit() {
        List<MessageState> mergesResult = messageStateDAO.findByType(MergeResult.class.getSimpleName()).all();
        List<MessageState> splits =  messageStateDAO.findByType(RegionSplit.class.getSimpleName()).all();
        splits.addAll(mergesResult);
        splits.sort(Comparator.comparing(MessageState::getTime));
        return splits;
    }

    Supplier<Collection<Map.Entry<Long, List<SolutionState>>>> regionSolutionsValuesOverTime(String regionName) {
        List<RegionState> regionStates = regionStateDAO.findByName(regionName).all();
        return  () -> {
            Map<Long, List<SolutionState>> solutionsOverTime = new TreeMap<>();


            for (RegionState regionState : regionStates) {
                List<SolutionState> solutions = solutionStateDAO.findByIds(regionState.getSolutions()).all();
                solutionsOverTime.put(regionState.getTime(), solutions);
            }

            return solutionsOverTime.entrySet();
        };
    }

    Supplier<Collection<AgentState>> agentStatesOverTime(String agentId) {
        return () -> {
            List<AgentState> agentStates = agentStateDAO.findByPersistentId(agentId).all();
            agentStates.sort(Comparator.comparing(AgentState::getTime));
            return agentStates;
        };
    }

    String globalStateRegionsOverTime(GlobalState gs) {
        return String.format("%d, %d\n", gs.getTime(), gs.getRegions());
    }

    String globalStatisticsOverTime(GlobalState gs) {
        String regionsIds = gs.getRegionIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        return String.format("%d, %f, %f, %d, \"[%s]\"\n", gs.getTime(), gs.getMean(), gs.getVariance(), gs.getSolutions(), regionsIds);
    }

    String regionSolutionOverTime(SolutionState ss) {
        return String.format("%d, %s\n", ss.getTime(), ss.getValues());
    }

    String bestSolutionOverTime(SolutionState ss) {
        return String.format("%d, %f, %s, \"%s\"\n", ss.getTime(), ss.getFunctionValue(), ss.getAgent(), ss.getValues());
    }

    String regionStatisticsOverTime(RegionState rs) {
        return String.format("%d, %f, %f, %d\n", rs.getTime(), rs.getMean(), rs.getVariance(), rs.getNumSolutions());
    }

    String messageStates(MessageState ms){
        return String.format("%d, %s, %d, %s, %s, %s\n", ms.getTime(), ms.getId(), ms.getTimestamp(),
                ms.getMessageType(), ms.getStatus(), ms.getEntityId());
    }

    String regionSolutionsValuesOverTime(Map.Entry<Long,List<SolutionState>> entry) {
        String positions = entry.getValue().stream().map(SolutionState::getValues)
                .map(x ->
                    "[" + x.stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                ).collect(Collectors.joining(", "));

        return String.format("%d, \"%s\"\n", entry.getKey(), positions);
    }

    String agentStatesOverTime(AgentState state) {
        return String.format("%d, %d, %d\n", state.getTime(), state.getTimestampStart(), state.getTimestampEnd());
    }

    public void extract(SolutionAnalyser analyser, String path) {
        logger.info("Starting global states extraction");

        this.analyser = analyser;
        this.path = path;

        File dataPath = new File(path);
        if (!dataPath.exists()) {
            boolean dirCreated = dataPath.mkdirs();
            if (dirCreated)
                logger.info(String.format("Data path %s created successfully", path));
            else
                logger.info(String.format("Couldn't create the data path %s", path));
        }

        process(globalStateSuppliers, globalStateFormatters);
        process(regionStateSuppliers, regionStateFormatters);
        process(solutionStateSuppliers, solutionStateFormatters);
        process(messageStateSuppliers, messageStateFormatters);
        process(regionSolutionsSuppliers, regionSolutionsFormatters);
        process(agentStateSuppliers, agentStateFormatters);

        logger.info("Finished processing all extractors");
    }

    <T> void  process(Map<String, Supplier<Collection<T>>> suppliers, Map<String, Function<T, String>> formatters) {
        try {
            for (String key : suppliers.keySet()) {
                logger.info("Processing " + key);
                Collection<T> data = suppliers.get(key).get();
                logger.info(data.toString());
                logger.info("Recovered " + data.size() + " States");

                File file = new File(path + key + ".csv");

                if (!file.exists()) {
                    boolean fileCreated = file.createNewFile();

                    if (fileCreated)
                        logger.info(String.format("File %s created successfully", file.getName()));
                    else
                        logger.info(String.format("Couldn't create file %s", file.getName()));
                }

                FileWriter writer = new FileWriter(file);
                logger.info("Writing " + key + " to file " + file.getName());
                for (T t : data) {
                    logger.debug("writing state: " + t);
                    if (t != null) {
                        String line = formatters.get(key).apply(t);
                        write(writer, line);
                    }
                }

                //data.stream()
                //        .filter(Objects::nonNull)
                //        .map(formatters.get(key))
                //        .forEachOrdered(line -> write(writer, line));

                writer.flush();
                writer.close();
            }
        } catch (IOException ex) {
            logger.info("", ex);
        }
    }

    void write(FileWriter writer, String line) {
        try {
            logger.debug(String.format("Writing line %s to file %s", line, writer.toString()));
            writer.write(line);
        } catch (IOException ex) {
            logger.error("", ex);
        }
    }
}
