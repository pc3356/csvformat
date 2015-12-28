package com.epeirogenic.csvformat;

/**
 * Created by IntelliJ IDEA.
 * User: pjc
 * Date: 27/12/2015
 * Time: 13:02
 */
public class DateColumnModification {

    private final int columnNumber;
    private final String dateFormat;

    private final DateModifier dateModifier = new DateModifier();

    public DateColumnModification(int columnNumber, String dateFormat) {
        this.columnNumber = columnNumber;
        this.dateFormat = dateFormat;
        dateModifier.setOriginalFormat(null);
        dateModifier.setTargetFormat(null);
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String transform(final String source) {

        return null;
    }
}
