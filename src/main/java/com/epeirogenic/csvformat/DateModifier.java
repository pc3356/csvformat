package com.epeirogenic.csvformat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by philipcoates on 24/12/2015.
 *
 */
public class DateModifier implements ColumnModifier {

    private SimpleDateFormat originalFormat;
    private SimpleDateFormat targetFormat;

    public SimpleDateFormat getOriginalFormat() {
        return originalFormat;
    }

    public void setOriginalFormat(SimpleDateFormat originalFormat) {
        this.originalFormat = originalFormat;
    }

    public DateModifier withOriginalFormat(final String originalFormat) {
        setOriginalFormat(new SimpleDateFormat(originalFormat));
        return this;
    }

    public SimpleDateFormat getTargetFormat() {
        return targetFormat;
    }

    public void setTargetFormat(SimpleDateFormat targetFormat) {
        this.targetFormat = targetFormat;
    }

    public DateModifier withTargetFormat(final String targetFormat) {
        setTargetFormat(new SimpleDateFormat(targetFormat));
        return this;
    }

    @Override
    public void modifyColumn(final int columnNumber) throws Exception {

        validateState();



    }

    @Override
    public String transformValue(final String source) throws ParseException {

        final Date date = originalFormat.parse(source);
        return targetFormat.format(date);
    }

    private void validateState() throws Exception {

        if(originalFormat == null || targetFormat == null) {

            final StringBuilder message = new StringBuilder();
            if(originalFormat == null) {
                message.append("Original format must be set");
            }
            if(targetFormat == null) {
                if(message.length() > 0) {
                    message.append("\n");
                }
                message.append("Target format must be set");
            }
            throw new Exception(message.toString());
        }
    }
}
