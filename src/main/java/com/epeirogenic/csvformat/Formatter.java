package com.epeirogenic.csvformat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by philipcoates on 24/12/2015.
 * Main application class: takes a bunch of command-line args:
 *  - which columns to adjust
 *  - what adjustments to make
 */
public class Formatter {

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

            final Formatter formatter = new Formatter();
            formatter.transformCsv(filename, 4, 5);

        } else {
            System.err.println("Usage: cmd -csv <csv file name>");
            return;
        }

    }

    public void transformCsv(final String filename, final int ... columns) throws IOException, ParseException {

        final Reader reader = new FileReader(filename);
        final Writer writer = new FileWriter(String.format("output/%1$s", FilenameUtils.getName(filename)));
        final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                "Service",
                "Operation",
                "UsageType",
                "Resource",
                "StartTime",
                "EndTime",
                "UsageValue")
        );

        int i = 0;
        final Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
        for (final CSVRecord record : records) {

            final DateModifier dateModifier = new DateModifier();
            dateModifier.setOriginalFormat(new SimpleDateFormat("MM/dd/yy HH:mm:SS"));
            dateModifier.setTargetFormat(new SimpleDateFormat("MMM yyyy"));

            final String[] values = new String[record.size()];
            final String fromDate = dateModifier.transformValue(record.get(4));
            final String toDate = dateModifier.transformValue(record.get(5));

            System.out.printf("Record %1$d: start: %2$s, end: %3$s\n", i++, fromDate, toDate);

            csvPrinter.printRecord(
                    record.get(0),
                    record.get(1),
                    record.get(2),
                    record.get(3),
                    fromDate,
                    toDate,
                    record.get(6)
            );
        }

        csvPrinter.flush();
        csvPrinter.close();
    }
}
