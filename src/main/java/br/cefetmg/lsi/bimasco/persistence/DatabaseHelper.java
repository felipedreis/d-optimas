package br.cefetmg.lsi.bimasco.persistence;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.ProgrammaticDriverConfigLoaderBuilder;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.time.Duration;

import static java.lang.String.format;

public class DatabaseHelper {

    private static final int MAX_TRIES = 10;

    private static CqlSession cqlSession;

    private static DOptimasMapper mapper;

    private static Logger logger = Logger.getLogger(DatabaseHelper.class);

    public static void initCqlSession(String host) {
        if (cqlSession == null) {
            logger.info(format("Initializing cassandra connection at address %s:9042", host));

            try {
                connect(host);
                checkDatabase();
            } catch (Exception ex) {
                logger.fatal("Can't connect to the database after " + MAX_TRIES + " tries");
            }
        }
    }

    public static void connect(String host) throws Exception {
        boolean connected = false;
        int tries = 0;
        long sleepTime = 10000;
        while (!connected) {
            try {
                logger.info("Trying to connect to cassandra");
                ProgrammaticDriverConfigLoaderBuilder loader = DriverConfigLoader.programmaticBuilder();
                loader.withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(10));

                cqlSession = CqlSession.builder().addContactPoint(new InetSocketAddress(host, 9042))
                        .withLocalDatacenter("datacenter1")
                        .withConfigLoader(loader.build())
                        //.withAuthCredentials("cassandra", "cassandra")
                        .build();
                connected = true;
            } catch (Exception ex) {
                logger.warn(format("Couldn't connect to the database yet, waiting %d millis", sleepTime));
                Thread.sleep(sleepTime);
                tries++;
                sleepTime += 1000;
                if (tries > MAX_TRIES)
                    throw new IllegalStateException("Max tries to connect reached");
            }
        }
    }

    public static void checkDatabase() throws Exception {
        boolean agreed;
        int tries = 0;
        do {
            Thread.sleep(10000);

            if (tries > MAX_TRIES) {
                throw new IllegalStateException("Could t agree on schema");
            }

            logger.info("checking schema agreement");
            agreed = cqlSession.checkSchemaAgreement();
            tries++;
        } while (!agreed);
    }


    public static CqlSession getCqlSession() {
        return cqlSession;
    }

    public static DOptimasMapper getMapper() {
        if (mapper == null) {
            DOptimasMapperBuilder builder = new DOptimasMapperBuilder(getCqlSession());
            mapper = builder.withDefaultKeyspace("d_optimas").build();
        }
        return mapper;
    }

    public static void clearAllTables(){
        logger.info("Cleaning up d-optimas tables");
        getMapper().agentStateDAO().clearTable();
        getMapper().globalStateDAO().clearTable();
        getMapper().memoryStateDAO().clearTable();
        getMapper().messageStateDAO().clearTable();
        getMapper().regionStateDAO().clearTable();
        getMapper().solutionStateDAO().clearTable();
    }
}
