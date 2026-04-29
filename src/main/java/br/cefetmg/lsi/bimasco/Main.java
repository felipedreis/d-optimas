package br.cefetmg.lsi.bimasco;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import br.cefetmg.lsi.bimasco.actors.DatabaseCleaner;
import br.cefetmg.lsi.bimasco.actors.MainActor;
import br.cefetmg.lsi.bimasco.data.DataExtractionBatch;
import br.cefetmg.lsi.bimasco.data.ExtractorsConfig;
import br.cefetmg.lsi.bimasco.persistence.DOptimasMapper;
import br.cefetmg.lsi.bimasco.persistence.DatabaseHelper;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import coco.CocoJNI;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;

public class Main {
    final static Options options = new Options();
    final static Logger logger = LoggerFactory.getLogger(Main.class);
    static {
        Option help = Option.builder("help")
                .desc("show this message")
                .build();

        Option host = Option.builder("host")
                .hasArg()
                .argName("ip")
                .build();

        Option config = Option.builder("config")
                .hasArg()
                .argName("file")
                .build();


        Option extract = Option.builder("extract")
                .build();

        options.addOptionGroup(
                new OptionGroup()
                        .addOption(help));

        options.addOption(host)
                .addOption(config)
                .addOption(extract);

    }

    public static void main(String[] args) {
        logger.info("Starting D-Optimas");

        try {
            CocoJNI.cocoSetLogLevel("info");
        } catch (Throwable t) {
            logger.warn("Could not initialize CocoJNI. Benchmark problems might not work: " + t.getMessage());
        }

        CommandLineParser parser = new  DefaultParser();
        try {

            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("d-optimas", options);
                System.exit(0);
            }

            String host = cmd.getOptionValue("host", "127.0.0.1");
            Config config = ConfigFactory.load();

            DatabaseHelper.initCqlSession(host);

            ActorSystem system = ActorSystem.create("d-optimas", config);

            DatabaseCleaner cleaner = new DatabaseCleaner();
            cleaner.cleanup(system);
            ActorRef mainActor = system.actorOf(Props.create(MainActor.class), "main");

            if (cmd.hasOption("config")) {
                File configFile = new File(cmd.getOptionValue("config"));
                Config simulationConfig = ConfigFactory.parseFile(configFile);
                SimulationSettings settings = new SimulationSettings(simulationConfig);

                if (cmd.hasOption("extract")){
                    logger.info("Received extract option, I won't run any simulation");
                    DOptimasMapper mapper = DatabaseHelper.getMapper();
                    logger.info("Building extract beans");
                    ExtractorsConfig extractorsConfig = new ExtractorsConfig(mapper.agentStateDAO(), mapper.regionStateDAO(),
                            mapper.solutionStateDAO(), mapper.globalStateDAO(), mapper.messageStateDAO(), mapper.memoryStateDAO());
                    extractorsConfig.setProblemId(settings.getName());

                    logger.info("Creating data extraction batch");
                    DataExtractionBatch extractionBatch = new DataExtractionBatch(settings.getExtractPath() + "/",
                            extractorsConfig.extractors());
                    logger.info("Starting data extraction batch");
                    extractionBatch.prepareDataPath();
                    extractionBatch.run();
                } else {
                    DatabaseHelper.clearAllTables();
                    mainActor.tell(settings, ActorRef.noSender());
                }
            } else {
                logger.info("D-Optimas started in dormant mode. Waiting for configuration.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

}
