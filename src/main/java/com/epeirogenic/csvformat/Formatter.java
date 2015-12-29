package com.epeirogenic.csvformat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by philipcoates on 24/12/2015.
 * Main application class: takes a bunch of command-line args:
 *  - which columns to adjust
 *  - what adjustments to make
 */
public class Formatter {

    private final static Logger LOGGER = LoggerFactory.getLogger(Formatter.class);

    private Map<Modifier, ColumnModifier> modifiers = new HashMap<>();

    public static void main(final String[] args) throws Exception {

        final Options options = new Options()
                .addOption("csv", true, "Absolute filename of source CSV");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);

        if(commandLine.hasOption("csv")) {

            final String filename = commandLine.getOptionValue("csv");

            final File output = new File("output");
            if(!output.exists()) {
                output.mkdirs();
            }

            final File input = new File(filename);
            if(input.exists()) {
                final Formatter formatter = new Formatter();

                if(input.isDirectory()) {
                    for(final File file : input.listFiles((dir, name) -> { return name.endsWith(".csv"); })) {
                       formatter.transformCsv(file, 4, 5);
                    }
                } else if(input.isFile()) {
                    formatter.transformCsv(input, 4, 5);
                }
                return;
            }

        }

        LOGGER.error("Usage: cmd -csv <csv file name or directory (must exist)>");
    }

    public void transformCsv(final File file, final int ... columnsToModify) throws Exception {

        final Reader reader = new FileReader(file);
        final String output = String.format("output/%1$s", FilenameUtils.getName(file.getName()));
        final Writer writer = new FileWriter(output);
        final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                "Service",
                "Operation",
                "UsageType",
                "Resource",
                "StartTime",
                "EndTime",
                "UsageValue")
        );

        final Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
        int i = 0;
        for (final CSVRecord record : records) {

            modifiers.put(Modifier.DATE, new DateModifier()
                    .withOriginalFormat("MM/dd/yy HH:mm:SS")
                    .withTargetFormat("MMM yyyy")
            );

            try {
                csvPrinter.printRecord(
                        modifyDate(0, record, columnsToModify),
                        modifyDate(1, record, columnsToModify),
                        modifyDate(2, record, columnsToModify),
                        modifyDate(3, record, columnsToModify),
                        modifyDate(4, record, columnsToModify),
                        modifyDate(5, record, columnsToModify),
                        modifyDate(6, record, columnsToModify)
                );
                i++;
            } catch(RuntimeException e) {
                LOGGER.error("Failed to parse {} (line {})", file.getName(), i, e);
                throw e;
            }
        }

        csvPrinter.flush();
        csvPrinter.close();
    }

    private String modifyDate(final int column, final CSVRecord record, final int[] columnsToModify) throws Exception {

        return modify(column, record, columnsToModify, Modifier.DATE);
    }

    private String modify(final int column, final CSVRecord record, final int[] columnsToModify, final Modifier type)
            throws Exception {

        for(final int col : columnsToModify) {
            if(col == column) {
                return modifiers.get(type).transformValue(record.get(column));
            }
        }
        return record.get(column);
    }

    private enum Modifier {
        DATE
    }
}
