package br.cefetmg.lsi.bimasco.actors;

import akka.actor.ActorSystem;
import akka.persistence.cassandra.cleanup.Cleanup;
import akka.persistence.cassandra.query.javadsl.CassandraReadJournal;
import akka.persistence.query.PersistenceQuery;
import org.apache.log4j.Logger;
import scala.compat.java8.FutureConverters;


public class DatabaseCleaner {
    private static final Logger logger = Logger.getLogger(DatabaseCleaner.class);

    public void cleanup(ActorSystem system) {

        logger.info("cleaning preavious logs");
        CassandraReadJournal queries = PersistenceQuery.get(system)
                .getReadJournalFor(CassandraReadJournal.class, CassandraReadJournal.Identifier());

        Cleanup cleanup = new Cleanup(system);

        int persistenceIdParallelism = 10;

        queries.currentPersistenceIds().mapAsync(persistenceIdParallelism,
                pid -> {
                    logger.info("Deleting pid " + pid);
                    return FutureConverters.toJava(cleanup.deleteAll(pid, false));
                }).run(system);

//        queries.currentPersistenceIds()
//                .mapAsync(persistenceIdParallelism,
//                        pid -> FutureConverters.toJava(cleanup.deleteAllSnapshots(pid)))
//                .run(system);
    }
}
