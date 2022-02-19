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
                    String novaSolucao = solution.getFunctionValue().toString();
                    String melhorSolucao = this.bestSolution.getFunctionValue().toString();
                    //System.out.println("Nova solucao gerada: " + novaSolucao);
                    //System.out.println("Melhor solucao: " + bestSolution);
                    if ((Double.parseDouble(novaSolucao) <
                            Double.parseDouble(melhorSolucao))
                            && this.simulationHasCooperation){
                        //this.reinforce(emitter);
                        makeBetter = true;
                        //System.out.println("	" + agentName + " melhorou sua solucao a partir do agente de id " + AgentDB.getById(emitter).getName());
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
        /*System.out.println("Memoria do Agente: " + agentName);
		for(Solucao solucao : getSolutions()){
			System.out.println("		Solucao " + getSolutions().indexOf(solucao) + ": " + solucao.getValorFuncao());
		}*/
    }

    public void printProbabilities(String agentName, UUID idAgent) {
        // TODO Auto-generated method stub
        if (probabilities.size() > 0) {
            //System.out.println("	Agente: " + agentName + " e suas probabilidades de escolha");
            for (int i = 0; i < probabilities.size(); i++) {
                Probability pro = probabilities.get(i);
                String agente = pro.getIdAgent().toString();
                String probabilidade = pro.getProbability().toString();
                //System.out.println("		Agente: " + agente);
                //System.out.println("		Probabilidade: " + probabilidade);
                //AgentChooseProbabilityDB.insertValues(idAgent, pro.getIdAgent(), ArtificialWorld.getSimulation().getId(), pro.getProbability(), System.currentTimeMillis() - ArtificialWorld.getTempoInicioExecucao());
            }
        }
    }

    private void reinforce(Integer emitter) {
        Double oldProb = 0.0;
        Double newProb = 0.0;
        Integer agenteSize = probabilities.size();
        for (Probability prob : probabilities) {
            if (prob.getIdAgent().equals(emitter)) {
                oldProb = prob.getProbability();
                newProb = prob.getProbability() + (1 - prob.getProbability()) / (agenteSize);
                prob.setProbability(newProb);
            }
        }
        for (Probability prob : probabilities) {
            if ((prob.getProbability() <= (newProb - oldProb) / (agenteSize)) && (!prob.getIdAgent().equals(emitter))) {
                agenteSize--;
            }
        }
        for (Probability prob : probabilities) {
            if ((!prob.getIdAgent().equals(emitter)) && (prob.getProbability() > (newProb - oldProb) / (agenteSize - 1)) && agenteSize != 0) {
                prob.setProbability(prob.getProbability() - (newProb - oldProb) / (agenteSize - 1));
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

    public void addSolutions(List<Solution> solucoes, UUID emitter) {

        //if (solucoes.size() > 1) {
        //    System.out.println("	ALGORITMO GENETICO	");
        //    System.out.println("	Size maior que zero...");
        //    System.out.println("	TAM: " + solucoes.size());
        //}

        for (Solution solution : solucoes) {
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
                Double valor1 = Double.parseDouble(getSolutions().get(j).getFunctionValue().toString());
                Double valor2 = Double.parseDouble(getSolutions().get(j + 1).getFunctionValue().toString());
                if (!this.problemSettings.getMax()) {
                    if (valor1 > valor2) {
                        aux = solutionsList.get(j);
                        solutionsList.set(j, solutionsList.get(j + 1));
                        solutionsList.set(j + 1, aux);
                    }
                } else {
                    if (valor1 < valor2) {
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

    public ArrayList<Solution> getSolucoes(Integer numberOfSolutions) {
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
        //this.agenteSize = agents.size();

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
                    adicionaProbabilities(agent.getAgentSettings().getId());
                } else {
                    if (agent.getAgentSettings().getConstructorMetaHeuristic()) {
                        adicionaProbabilities(agent.getAgentSettings().getId());
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
				removeProbabilities(prob.getIdAgent());
		}*/
        //System.out.println("Probabilidades: " + probabilities.size()
        //		+ " - Agente : " + idAgent);
		/*for(Agent newAgent : agents){
			boolean alreadyExist = false;
			for(Agent agent : this.agentes){
				if(newAgent.getId().equals(agent.getId())){
					int id = this.agentes.indexOf(agent);
					this.agentes.set(id, newAgent);
					alreadyExist = true;
				}
			}
			if(!alreadyExist){
				this.agentes.add(newAgent);
				adicionaProbabilities(newAgent.getId());
			}
		}
		int tam = this.agentes.size();
		ArrayList<Agent> remover = new ArrayList<Agent>();
		for(int i=0; i < tam; i++){
			Agent agent = this.agentes.get(i);
			if(!agents.contains(agent)){
				remover.add(agent);
			}
		}
		for(Agent agent : remover){
			this.agentes.remove(agent);
			removeProbabilities(agent.getId());
		}*/
    }

    public void adicionaProbabilities(UUID emitter) {
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

    public void removeProbabilities(Integer emitter) {
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