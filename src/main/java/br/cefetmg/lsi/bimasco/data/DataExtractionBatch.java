package br.cefetmg.lsi.bimasco.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DataExtractionBatch {
    private static final Logger logger = LoggerFactory.getLogger(DataExtractionBatch.class);

    private List<Extractor<?>> extractors;

    private String path;

    private String problem;

    public DataExtractionBatch(String path, List<Extractor<?>> extractors) {
        this.path = path;
        this.extractors = extractors;
    }

    public void prepareDataPath() throws IOException {
        logger.info("Preparing data dir");
        Path dataPath = Paths.get(path);

        if (Files.exists(dataPath))
            Files.delete(dataPath);

        Files.createDirectories(dataPath);
    }


    public void run() {

        logger.info("Starting data extraction to " + path);
        for (Extractor<?> extractor : extractors) {
            logger.info("Running extractor " +  extractor.getClass().getSimpleName());
            extractor.extractData(path);
        }
        logger.info("Finished data extraction successfully");
        System.exit(0);
    }
}
