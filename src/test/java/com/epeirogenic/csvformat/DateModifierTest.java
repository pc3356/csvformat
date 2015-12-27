package com.epeirogenic.csvformat;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by philipcoates on 24/12/2015.
 *
 */
public class DateModifierTest {

    @Test
    public void should_convert_formats() throws Exception {

        final String source = "12/01/2014 00:00:00";
        final String expected = "Dec 2014";

        final DateModifier dateModifier = new DateModifier();

        dateModifier.setOriginalFormat(new SimpleDateFormat("MM/dd/yyyy HH:mm:SS"));
        dateModifier.setTargetFormat(new SimpleDateFormat("MMM yyyy"));

        final String actual = dateModifier.transformValue(source);

        assertThat(actual, is(expected));
    }

    @Test
    public void should_extract_filename() throws Exception {

        final String filename = "/tmp/csv/ec2_report.csv";
        final String name = FilenameUtils.getName(filename);
        assertThat(name, is("ec2_report.csv"));
    }
}