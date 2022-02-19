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

//TODO: Rename elements
public class GreedyHeuristic extends MetaHeuristic {

    public static int c_TimeDivisor = 1000;
    // Inclusao: Verificar como isso pode modificar a questão da alterações do parametros do agente
    //private ICondicaoParada condicaoParada;
    private List<Solution> melhorSolucao;
    private Solution solucaoCorrente;
    private Integer maxIteracoes;
    private String nomeLCandidatos;
    private SolutionAnalyser solutionAnalyser;
    private Object limite;

    public GreedyHeuristic(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {
        this.metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        this.maxIteracoes = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 0);

        this.nomeLCandidatos = (String) metaHeuristicParameters
                .get(DefaultMetaHeuristicParametersKeySupported.CANDIDATES_LIST_NAME_KEY);


        this.limite = this.problem.getLimit();


        this.solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        if (problem.getProblemSettings().getName() != null) {
            this.solucaoCorrente = Solution.buildSolution(problem);
        }

        this.melhorSolucao = new ArrayList<>();
        this.melhorSolucao.add(0, this.solucaoCorrente);
    }

    @SuppressWarnings("empty-statement")
    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {
        double initialTime = 0;
        double finalTime = 0;
        double end = 0;

        initialTime = System.currentTimeMillis();

        System.out.println("\nSOLUÇÃO INICIAL DO GreedyHeuristic! ");
        System.out.println("\nMÉTODO GreedyHeuristic! ");

        metodo_Gulosa(context);

        finalTime = System.currentTimeMillis();
        end = ((finalTime - initialTime) / c_TimeDivisor);
        System.err.println("\nEnded - GreedyHeuristic = Time spent: " + end + "");

        System.gc();

        return (this.melhorSolucao);
    }

    public void metodo_Gulosa(Context context) {
        List<List<Object>> lCandidatos = new ArrayList<>();
        CandidatesList listaCandidatos = null;
        Solution solucaoAux = null;
        Solution solucaoElemento = null;

        Random rand = new Random();
        int divisor = 0;
        int percentagem = 0;
        int nElementos = 0;
        int indexMC;
        Object sA = null;
        Object melhor = null;
        Object mS = this.solucaoCorrente.getFunctionValue();
        Object sE = null;

        nElementos = problem.getDimension();
        percentagem = (int) (0.1 * nElementos);

        this.limite = this.problem.getLimit();


        if (percentagem <= 0) {
            percentagem = 1;
        }

        for (int i = 0; i < this.maxIteracoes; i++) {
            listaCandidatos = CandidatesListHelper.buildCandidatesList(this.nomeLCandidatos, this.problem);
            //lCandidatos = listaCandidatos.getCandidates();

            solucaoAux = Solution.buildSolution(problem);
            solucaoElemento = Solution.buildSolution(problem);

            for (int j = 0; j < percentagem; j++) {
                indexMC = rand.nextInt(lCandidatos.size());
                //solucaoAux.addElement(lCandidatos.get(indexMC));
                if (!solucaoAux.isViable(context)) {
                    List<Object> candidate = lCandidatos.get(indexMC);

                    solucaoAux.removeElement((int) candidate.get(0));
                }

                //lCandidatos = listaCandidatos.removeCandidates(indexMC);
            }

            sA = solucaoAux.getFunctionValue();

            while (lCandidatos.size() != 0) {
                indexMC = -1;
                melhor = this.limite;

                for (int j = 0; j < lCandidatos.size(); j++) {
                    //solucaoElemento.copyValues(solucaoAux);
                    //solucaoElemento.addElementToSolution(lCandidatos.get(j));
                    sE = solucaoElemento.getFunctionValue();

                    //if (sE.equals(this.solutionAnalyser.getBest(melhor, sE))) {
                    //    if (solucaoElemento.isViable(context)) {
                    //        melhor = sE;
                    //        indexMC = j;
                    //    }
                    //}
                }

                if (indexMC == -1) {
                    lCandidatos.clear();
                } else {
                    //solucaoAux.addElementToSolution(lCandidatos.get(indexMC));
                    //lCandidatos = listaCandidatos.removeCandidates(indexMC);
                    sA = solucaoAux.getFunctionValue();
                }
            }


            //if (sA.equals(this.solutionAnalyser.getBest(sA, mS))) {
            //    solucaoCorrente = solucaoAux;
            //    mS = sA;
            //}

            if (divisor == 0)
                divisor = ((int) (this.maxIteracoes / 10));
            if (i % (divisor < 2 ? 2 : divisor) == 0)
                System.gc();
        }

        melhorSolucao.clear();
        melhorSolucao.add(solucaoCorrente);

    }

    public Integer melhorElemento(Solution SCorrente, List<List<Object>> LCandidatos) {
        Solution SAuxiliar = null;

        SAuxiliar = Solution.buildSolution(problem);

        int indexMS = 0;

        SAuxiliar = (Solution) SCorrente.clone();
        //SAuxiliar.addElementToSolution(LCandidatos.get(indexMS));
        Object valorMS = SAuxiliar.getFunctionValue();
        Object valorSA = null;

        for (int i = 1; i < LCandidatos.size(); i++) {
            SAuxiliar = (Solution) SCorrente.clone();
            //SAuxiliar.addElementToSolution(LCandidatos.get(i));
            valorSA = SAuxiliar.getFunctionValue();

            //if (valorSA.equals(this.solutionAnalyser.getBest(valorSA, valorMS))) {
            //    valorMS = valorSA;
            //    indexMS = i;
            //}
        }

        return indexMS;
    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
    }

    public Integer getMaxIteracoes() {
        return maxIteracoes;
    }

    public void setMaxIteracoes(Integer maxIteracoes) {
        this.maxIteracoes = maxIteracoes;
    }
}
