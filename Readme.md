Bimasco Readme
=============

The propose of this file is provide a overview of Bimasco architecture.
* Obs.: This file is not complete.

Here we must include a project overview 


0. You are welcome to help make this file.

Please see our [contributing guidelines](CONTRIBUTING.md) before start work.

Requirements (to build from source)
-------

0. Java SDK 8u65 or above
0. PostgreSQL 9.4.5 or above
0. Maven dependency tool
0. Set a system variable _JAVA_OPTIONS with value '-Xmx512M'

Desired
------

0. IntelliJ 15.0.1 or above (because IntelliJ is the best)
0. IntelliJ plugin scala support
0. pgAdmin 3 or above

* [.pod](http://search.cpan.org/dist/perl/pod/perlpod.pod) -- `Pod::Simple::HTML`
  comes with Perl >= 5.10. Lower versions should install [Pod::Simple](http://search.cpan.org/~dwheeler/Pod-Simple-3.28/lib/Pod/Simple.pod) from CPAN.

Installation
-----------

```
1 - Create a new Database with name "Bimasco2.0"
2 - run all tests "src/test"
```

Usage
-----

```
1 - To use Bimasco you must set a "simulationSettings.properties"
on "src/main/resources/settings/simulationSettings.properties"

2 - run "br.cefetmg.lsi.bimasco.Main"
```


Project structure
----------------

The Bimasco's project architecture is organized by:

* **br.cefetmg.lsi.bimasco**
    * **actors** = agent and region actor implementations using akka.
    * **experiment** (this was used only for a test propose)
    * **messages** = messages changed between actors
    * **persistence** = all necessary to persist any data. At now we have only PostgreSQL implementation.
    * **settings** = all necessary to manage settings files of Bimasco's simulations.
    * **core** = all basic components of bimasco architecture.
        * **agents** = all components necessary to create an agent as Memory for instance
        * **regions** = region implementation
        * **heuristics** = all components necessary to create some heuristic
            * **localSearch** = all local search available on Bimasco
            * **metaHeuristics** = all meta heuristics available on Bimasco
            * **stopCondition** = all stop conditions available on Bimasco
        * **problems** = all components necessary to create a problem (all problems implementations)
            * **candidatesList** = all implementations of candidates list choice available on Bimasco
            * **functions** = all function problems implementations available on Bimasco
        * **solutions** = all components necessary to create a solution
            * **analyser** = all solutions analyser implementations available on Bimasco
            * **solutionModifier** = all solutions modifies implementations available on Bimasco
            * **modifiesSolutionCollections** = all solutions collections modifies implementations available on Bimasco
            * **motion** = all motion solutions implementations available on Bimasco
            * **motionCollections** = all motion collection solutions implementations available on Bimasco
            * **neighborsList** = all type of neighbors list choice implemented on Bimasco
        * **utils** = helper classes


Basic Components Implemented
-----------

* **localSearch**
    * **BetterNeighborhoodLocalSearch** =
    * **FirstImproveLocalSearch** =
    * **RandomLocalSearch** =

* **metaHeuristics**
    * **AG_P** =
    * **GRASP_P** =
    * **GRASP_NP** =
    * **HGulosa** =
    * **HRandom** =
    * **ILS** =
    * **SA** =
    * **VNS** =

* **stopCondition**
    * **F0StopCondition** =
    * **MaxIterationsStopCondition** =
    * **MaxIterationsWithoutImproveStopCondition** =
    * **MixStopCondition** =
    * **TimeStopCondition** =

* **candidatesList**
    * **PositionCandidatesList** =
    * **SolutionElementsCountCandidatesList** =
    * **StepCandidatesList** =

* **functions** = mathematical functions that can be used on function's problem.
    * **BealeFunction** =
    * **EasomFunction** =
    * **EggHolderFunction** =
    * **GriewankFunction** =
    * **MichalewicsFunction** =
    * **RastriginFunction** =
    * **SumSquaresFunction** =
    * **XinSheYang03Function** =

* **problems**
    * **BinaryPartitionNumberProblem** =
    * **FunctionProblem** =
    * **Knapsack01Problem** =
    * **PartitionNumberProblem** =
    * **PDMProblem** =
    * **PRVJT_DeniseProblem** =

* **solutions** each problem has your solution
    * **BinaryPartitionNumberSolution** =
    * **FunctionSolution** =
    * **Knapsack01Solution** =
    * **PartitionNumberSolution** =
    * **PDMSolution** =
    * **PRVJT_DeniseSolution** =

* **solutionModifier** = each kind of solution has your solution analyser
* **modifiesSolutionCollections** = each kind of solution has your solution analyser
* **motion** = each kind of solution has your solution analyser
* **motionCollections** = each kind of solution has your solution analyser
* **neighborsList** = each kind of solution has your solution analyser

* **analyser** = each kind of solution has your solution analyser


Overview actors and messages
-----------

### Actors

One **actor**, as a actor model proposed by Carl's Hewitt, is responsible to receive a message, process some work and send zero or more messages to the others actors.
On Bimasco each **agent** or **region** are implemented as a **actor**. This actors (agents or regions) interact by messages.

### Messages

Todas as mensagens são categorizadas em:

* **ExternalStimulus** = are external messages exchange between regions and agents.
* **InternalStimulus** = are internal messages exchange internally on regions or agents.

#### Messages Components

All components of a message are described on this section:

* **Payload**

Each message has on **Payload** structure to store information.

For **ExternalStimulus** messages are used one of this **Payload**

* AcknowledgePayload
* MergePayload
* SolutionPayload

For **InternalStimulus** messages are not used **Payload**

* **StimulusInformation**

Each message has on **StimulusInformation** used to flag what is the message subject.

For **ExternalStimulus** messages are used one of this **StimulusInformation**

* ACKNOWLEDGE
* MERGE_REQUEST
* MERGE_RESPONSE
* SOLUTION_REQUEST
* SOLUTION_RESULT
* REGION_MERGED
* BOOTSTRAP

For **InternalStimulus** messages are used one of this **StimulusInformation**

* CHANGE_MY_STATE


Architecture workflow
------------

Basically the flow of messages exchange on Bimasco follow this order:

## Ping

Each 5 seconds all actors on Bimasco send a **ExternalStimulus** as a Ping to the others agents. This is necessary to
keep every actor updated about the others.

Agent actors constantly send **ExternalStimulus** with ACKNOWLEDGE **StimulusInformation** only to others agents actor.
Regions actors constantly send **ExternalStimulus** with ACKNOWLEDGE **StimulusInformation** to others agents and regions actor.

## Constructors Agents


## Observações

public static String getTipo(){
        if(idTipo==0){
            return "Inteiro";
        } else if(idTipo==1){
            return "Binario";
        } else if(idTipo==2){
            return "Real";
        }
        return null;
    }


Contributing
------------

Before contribute see our adopted principles always as possible:

1. Prefira sempre **convenção** ao invés de **configuração**;
1. **Nunca** oriente-se por BD. O BD deve apenas auxiliar no armazenamento de eventos ocorridos na arquitetura;
1. Reutilização de boas bibliotecas (configuração/ORM/cálculos matemáticos)
1. A aplicação deve ser standalone (não deve precisar de nada além da JVM e do arquivo .JAR)
1. Testes de unidade sempre que possível
1. Comentários inúteis são inúteis;
1. Convenção de codificação padrão JAVA;
1. Evitar, sempre que possível, o uso de operadores estáticos;
1. Lembre-se, código comentado é inútil (serve apenas para confundir)
1. A inicialização de um ator não deve disparar mensagens. Utilizar sempre que necessário mensagens de controle.
Isso facilita o processo de testes.

* Como primeira abordagem serão salvos referencias (mesmo que não atualizadas) de agentes e regioes. No futuro é
possível que utilizemos apenas mensagem com ActorSelection


See [Contributing](CONTRIBUTING.md)
