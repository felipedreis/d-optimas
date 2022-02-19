package br.cefetmg.lsi.bimasco.data;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

public interface Extractor<T> {

    List<T> getData();
    String formatDataToCsv(T t);
    String getFileName();

    default void extractData(String path) {
        final Logger logger = Logger.getLogger(this.getClass());

        File file = new File(path + getFileName() + ".csv");

        try {
            if (!file.exists()) {
                logger.debug("creating file " +  getFileName());
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);

            List<T> data = getData();

            logger.info(format("Got %d lines for %s", data.size(), getFileName()));

            for (T t : data) {
                String line = formatDataToCsv(t);
                writer.write(line);
                writer.write('\n');
            }
            writer.close();
            logger.info(format("Extraction for %s finished successfully", getFileName()));

        } catch (IOException ex) {
            logger.warn(format("Error writing data for %s.csv file", getFileName()));
        }
    }
}
