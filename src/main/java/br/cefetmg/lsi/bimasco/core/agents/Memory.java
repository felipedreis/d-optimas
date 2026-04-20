package br.cefetmg.lsi.bimasco.core.agents;

import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.utils.BimascoDate;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;

import java.io.Serializable;
import java.util.*;

public class Memory implements Serializable {

    private static final int MEMORY_SIZE = 300;

    private ProblemSettings problemSettings;

    private UUID agentTargetRequest;
    private ArrayList<Solution> solutionsList;
    private ArrayList<Probability> probabilities;
    private Integer timesWithoutChangeTarget;
    private Integer actualMemoryPosition;
    private Solution bestSolution;
    private TimeLine timeLine;

    private boolean simulationHasCooperation;

    //TODO: Understand Memory Probability
    public Memory(ProblemSettings problemSettings, boolean hasCooperation) {

        this.problemSettings = problemSettings;
        this.simulationHasCooperation = hasCooperation;

        this.agentTargetRequest = null;
        this.solutionsList = new ArrayList<Solution>(MEMORY_SIZE);
        for (int i = 0; i < MEMORY_SIZE; i++)
            this.solutionsList.add(i, null);
        this.probabilities = new ArrayList<Probability>();
        this.timesWithoutChangeTarget = 0;
        this.actualMemoryPosition = 0;
        this.bestSolution = null;
        //TODO: Remove this comment
        this.timeLine = new TimeLine();
    }

    public void addAndCheckBestSolutions(List<Solution> solutionsList, String agentName, UUID idAgent, UUID emitter) {

        boolean makeBetter = false;
        if (this.bestSolution != null) {
            for (Solution solution : solutionsList) {
                if (this.problemSettings.getMax()) {
                    if ((Double.parseDouble(solution.getFunctionValue().toString()) >
                            Double.parseDouble(this.bestSolution.getFunctionValue().toString()))
                            && this.simulationHasCooperation){

                        //this.reinforce(emitter);
                        makeBetter = true;
                    }
                } else {
                    String newSolutionValue = solution.getFunctionValue().toString();
                    String bestSolutionValue = this.bestSolution.getFunctionValue().toString();
                    //System.out.println("New solution generated: " + newSolutionValue);
                    //System.out.println("Best solution: " + bestSolution);
                    if ((Double.parseDouble(newSolutionValue) <
                            Double.parseDouble(bestSolutionValue))
                            && this.simulationHasCooperation){
                        //this.reinforce(emitter);
                        makeBetter = true;
                        //System.out.println("	" + agentName + " improved its solution from agent id " + AgentDB.getById(emitter).getName());
                    }
                }
            }

            if(this.simulationHasCooperation){
                //if(makeBetter == false)
                //this.unforce(emitter);
            }
        }
        addSolutions(solutionsList, emitter);
    }

    public void printSolutions(String agentName)  {
        // TODO Auto-generated method stub
        /*System.out.println("Agent Memory: " + agentName);
		for(Solution solution : getSolutions()){
			System.out.println("		Solution " + getSolutions().indexOf(solution) + ": " + solution.getFunctionValue());
		}*/
    }

    public void printProbabilities(String agentName, UUID idAgent) {
        // TODO Auto-generated method stub
        if (probabilities.size() > 0) {
            //System.out.println("	Agent: " + agentName + " and its choice probabilities");
            for (int i = 0; i < probabilities.size(); i++) {
                Probability pro = probabilities.get(i);
                String agent = pro.getIdAgent().toString();
                String probability = pro.getProbability().toString();
                //System.out.println("		Agent: " + agent);
                //System.out.println("		Probability: " + probability);
                //AgentChooseProbabilityDB.insertValues(idAgent, pro.getIdAgent(), ArtificialWorld.getSimulation().getId(), pro.getProbability(), System.currentTimeMillis() - ArtificialWorld.getStartTimeExecution());
            }
        }
    }

    private void reinforce(Integer emitter) {
        Double oldProb = 0.0;
        Double newProb = 0.0;
        Integer agentSize = probabilities.size();
        for (Probability prob : probabilities) {
            if (prob.getIdAgent().equals(emitter)) {
                oldProb = prob.getProbability();
                newProb = prob.getProbability() + (1 - prob.getProbability()) / (agentSize);
                prob.setProbability(newProb);
            }
        }
        for (Probability prob : probabilities) {
            if ((prob.getProbability() <= (newProb - oldProb) / (agentSize)) && (!prob.getIdAgent().equals(emitter))) {
                agentSize--;
            }
        }
        for (Probability prob : probabilities) {
            if ((!prob.getIdAgent().equals(emitter)) && (prob.getProbability() > (newProb - oldProb) / (agentSize - 1)) && agentSize != 0) {
                prob.setProbability(prob.getProbability() - (newProb - oldProb) / (agentSize - 1));
            }
        }
    }

    private void unforce(Integer emitter) {
        Double oldProb = 0.0;
        Double newProb = 0.0;
        Integer size = probabilities.size();
        for (Probability prob : probabilities) {
            if (prob.getIdAgent().equals(emitter)) {
                oldProb = prob.getProbability();
                newProb = prob.getProbability() - (prob.getProbability()) / (size);
                prob.setProbability(newProb);
            }
        }
        for (Probability prob : probabilities) {
            if (!prob.getIdAgent().equals(emitter)) {
                prob.setProbability(prob.getProbability() + (oldProb - newProb) / (size - 1));
            }
        }
    }

    public void addSolutions(List<Solution> solutions, UUID emitter) {

        //if (solutions.size() > 1) {
        //    System.out.println("	GENETIC ALGORITHM	");
        //    System.out.println("	Size greater than zero...");
        //    System.out.println("	SIZE: " + solutions.size());
        //}

        for (Solution solution : solutions) {
            String time = BimascoDate.formattedTime();
            if (emitter != null)
                solution.setInitialSolutionCreator(emitter);
            this.getTimeLine().addGeneratedSolutions(solution.getFunctionValue().toString(), time);

            this.solutionsList.set(this.actualMemoryPosition, solution);
            if (this.bestSolution == null) {
                this.bestSolution = solution;
            } else {
                if (this.problemSettings.getMax()) {
                    if (Double.parseDouble(solution.getFunctionValue().toString()) >
                            Double.parseDouble(this.bestSolution.getFunctionValue().toString())) {
                        this.bestSolution = solution;
                    }
                } else {
                    if (Double.parseDouble(solution.getFunctionValue().toString()) <
                            Double.parseDouble(this.bestSolution.getFunctionValue().toString())) {
                        this.bestSolution = solution;
                    }
                }
            }
            if (actualMemoryPosition < (MEMORY_SIZE - 1)) {
                this.actualMemoryPosition++;
            }
        }
    }

    private boolean hasSolution(Solution sol) {

        for (Solution solution : getSolutions()) {
            if (solution.getFunctionValue().equals(sol.getFunctionValue())) {
                return true;
            }
        }
        return false;
    }

    public void sort() {

        Solution aux;
        for (int i = 0; i < getSolutions().size(); i++) {
            for (int j = 0; j < getSolutions().size() - 1; j++) {
                Double value1 = Double.parseDouble(getSolutions().get(j).getFunctionValue().toString());
                Double value2 = Double.parseDouble(getSolutions().get(j + 1).getFunctionValue().toString());
                if (!this.problemSettings.getMax()) {
                    if (value1 > value2) {
                        aux = solutionsList.get(j);
                        solutionsList.set(j, solutionsList.get(j + 1));
                        solutionsList.set(j + 1, aux);
                    }
                } else {
                    if (value1 < value2) {
                        aux = solutionsList.get(j);
                        solutionsList.set(j, solutionsList.get(j + 1));
                        solutionsList.set(j + 1, aux);
                    }
                }
            }
        }
    }

    public UUID getAgentTargetRequest() {
        return agentTargetRequest;
    }

    public void setAgentTargetRequest(UUID agentTargetRequest) {
        this.agentTargetRequest = agentTargetRequest;
    }

    public ArrayList<Solution> getSolutions() {
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        for (Solution sol : this.solutionsList) {
            if (sol != null)
                solutions.add(sol);
        }
        return solutions;
    }

    public void setSolutions(ArrayList<Solution> solutionsList) {
        this.solutionsList = solutionsList;
    }

    public Integer getTimesWithoutChangeTarget() {
        return timesWithoutChangeTarget;
    }

    public void setTimesWithoutChangeTarget(Integer timesWithoutChangeTarget) {
        this.timesWithoutChangeTarget = timesWithoutChangeTarget;
    }

    public ArrayList<Solution> getSolutionsByCount(Integer numberOfSolutions) {
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        if (getSolutions().size() > 0) {
            Random rand = new Random();
            int iterator = 0;
            int index = 0;
            while (iterator < numberOfSolutions) {
                index = rand.nextInt(getSolutions().size());
                if (this.getSolutions().get(index) != null) {
                    if (index < getSolutions().size()) {
                        solutions.add(this.getSolutions().get(index));
                        iterator++;
                    }
                }
            }
        }

        return solutions;
    }

    public Solution getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(Solution bestSolution) {
        this.bestSolution = bestSolution;
    }

    public void addAgents(Map<UUID, Agent> agents, UUID idAgent) {
        //this.agentSize = agents.size();

        ArrayList<Probability> probabilitiesList = (ArrayList<Probability>) this.probabilities.clone();
        for (Agent agent : agents.values()) {
            boolean alreadyExist = false;
            for (Probability prob : probabilitiesList) {
                if (prob.getIdAgent().equals(agent.getAgentSettings().getId()))
                    alreadyExist = true;
            }
            if (alreadyExist == false) {
                //TODO: Put a solution cooperation variable on problemSettings
                if(this.simulationHasCooperation){
                    addProbabilities(agent.getAgentSettings().getId());
                } else {
                    if (agent.getAgentSettings().getConstructorMetaHeuristic()) {
                        addProbabilities(agent.getAgentSettings().getId());
                    }
                }
            }
        }
		/*for(Probabilities prob : probabilitiesList){
			boolean alreadyExist = false;
			for(Agent agent : agents){
				if(agent.getId().equals(prob.getIdAgent()))
					alreadyExist = true;
			}
			if(alreadyExist == false && prob.getIdAgent() != 0)
				removeProbability(prob.getIdAgent());
		}*/
        //System.out.println("Probabilities: " + probabilities.size()
        //		+ " - Agent : " + idAgent);
		/*for(Agent newAgent : agents){
			boolean alreadyExist = false;
			for(Agent agent : this.agents){
				if(newAgent.getId().equals(agent.getId())){
					int id = this.agents.indexOf(agent);
					this.agents.set(id, newAgent);
					alreadyExist = true;
				}
			}
			if(!alreadyExist){
				this.agents.add(newAgent);
				addProbabilities(newAgent.getId());
			}
		}
		int size = this.agents.size();
		ArrayList<Agent> remove = new ArrayList<Agent>();
		for(int i=0; i < size; i++){
			Agent agent = this.agents.get(i);
			if(!agents.contains(agent)){
				remove.add(agent);
			}
		}
		for(Agent agent : remove){
			this.agents.remove(agent);
			removeProbability(agent.getId());
		}*/
    }

    public void addProbabilities(UUID emitter) {
        Integer size = probabilities.size();
        Probability probability = new Probability(emitter, (1.0 / (size + 1)));
        Double variation = probability.getProbability() / (size);
        for (Probability prob : probabilities) {
            if ((prob.getProbability() <= variation) && (!prob.getIdAgent().equals(emitter))) {
                size--;
                variation = probability.getProbability() / (size);
            }
        }

        for (Probability prob : probabilities) {
            if ((!prob.getIdAgent().equals(emitter)) && (prob.getProbability() > variation)) {
                prob.setProbability(prob.getProbability() - variation);
            }
        }
        probabilities.add(probability);
    }

    public void removeProbability(UUID emitter) {
        Integer size = this.probabilities.size() - 1;
        Double variation = 0.0;
        Probability remove = null;
        for (Probability prob : probabilities) {
            if (prob.getIdAgent().equals(emitter)) {
                variation = prob.getProbability();
                remove = prob;
            }
        }
        probabilities.remove(remove);

        for (Probability prob : probabilities) {
            prob.setProbability(prob.getProbability() + (variation) / (size));
        }
    }


    public ArrayList<Probability> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(ArrayList<Probability> probabilities) {
        this.probabilities = probabilities;
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(TimeLine timeLine) {
        this.timeLine = timeLine;
    }
}