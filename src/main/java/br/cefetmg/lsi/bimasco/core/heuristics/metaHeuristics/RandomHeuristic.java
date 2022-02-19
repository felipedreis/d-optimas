package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.candidatesList.CandidatesList;
import br.cefetmg.lsi.bimasco.core.problems.candidatesList.CandidatesListHelper;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: Rename elements
public class RandomHeuristic extends MetaHeuristic {

    public static int c_TimeDivisor = 1000;
    // Inclusao: Verificar como isso pode modificar a questão da alterações do parametros do agente
    //private ICondicaoParada condicaoParada;
    //private IModificaColecaoSolucoes solucaoInicial;
    private List<Solution> melhorSolucao;
    private Solution solucaoCorrente;
    private Integer maxIteracoes;
    private String nomeLCandidatos;
    private SolutionAnalyser solutionAnalyser;

    public RandomHeuristic(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {
        this.metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        this.maxIteracoes = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 0);

        this.nomeLCandidatos = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.CANDIDATES_LIST_NAME_KEY, "");


        this.solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);

        if (problem.getProblemSettings().getName() != null) {
            this.solucaoCorrente = Solution.buildSolution(problem);
        }


        this.melhorSolucao = new ArrayList<Solution>();
        this.melhorSolucao.add(0, this.solucaoCorrente);
    }

    @SuppressWarnings("empty-statement")
    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {
        double initialTime = 0;
        double finalTime = 0;
        double end = 0;

        initialTime = System.currentTimeMillis();

        System.out.println("\nSOLUÇÃO INICIAL DO RandomHeuristic! ");
        System.out.println("\nMÉTODO RandomHeuristic! ");

        try {
            this.metodo_HRandomica(context);
        } catch (Throwable ex) {
            Logger.getLogger(RandomHeuristic.class.getName()).log(Level.SEVERE, null, ex);
        }

        finalTime = System.currentTimeMillis();
        end = ((finalTime - initialTime) / c_TimeDivisor);
        System.err.println("\nEnded - RandomHeuristic = Time spent: " + end + "");

        System.gc();

        return (this.melhorSolucao);
    }

    public void metodo_HRandomica(Context context)  {
        List<List<Object>> lCandidatos = new ArrayList<>();
        List<Object> elemento = null;
        CandidatesList listaCandidatos = null;
        Solution solucaoAux = null;
        Random rand = new Random();
        Class object = null;
        Object ms = this.solucaoCorrente.getFunctionValue();
        Object sA = null;
        int divisor = 0;
        int iteracoes = 0;
        int index = 0;
        int tamLC = 0;

        while (iteracoes < maxIteracoes) {

            listaCandidatos = CandidatesListHelper.buildCandidatesList(this.nomeLCandidatos, this.problem);
            //lCandidatos = listaCandidatos.getCandidates();

            solucaoAux = Solution.buildSolution(problem);

            tamLC = lCandidatos.size();
            for (int i = 0; i < tamLC; i++) {
                index = rand.nextInt(lCandidatos.size());
                elemento = lCandidatos.get(index);
                //solucaoAux.addElementToSolution(elemento);
                //lCandidatos = listaCandidatos.removeCandidates(index);

                if (!solucaoAux.isViable(context)) {
                    //solucaoAux.removeElementOfSolution(elemento);
                }
            }

            sA = solucaoAux.getFunctionValue();

            //if (sA.equals(this.solutionAnalyser.getBest(sA, ms))) {
                //this.solucaoCorrente.copyValues(solucaoAux);
                ms = sA;
            //}

            iteracoes++;

            if (divisor == 0)
                divisor = ((int) (maxIteracoes / 10));
            if (iteracoes % (divisor < 2 ? 2 : divisor) == 0)
                System.gc();
        }

        melhorSolucao.clear();
        melhorSolucao.add(solucaoCorrente);

    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
    }
}
