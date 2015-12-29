package com.epeirogenic.csvformat;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: pjc
 * Date: 27/12/2015
 * Time: 13:02
 */
public class DateColumnModification {

    private final int columnNumber;

    private final DateModifier dateModifier;

    public DateColumnModification(int columnNumber, String originalFormat, final String targetFormat) {
        this.columnNumber = columnNumber;
        dateModifier = new DateModifier()
                .withOriginalFormat(originalFormat)
                .withTargetFormat(targetFormat);
    }

    public String transform(final CSVRecord record) throws ParseException {

        final String original = record.get(columnNumber);
        return dateModifier.transformValue(original);
    }
}
