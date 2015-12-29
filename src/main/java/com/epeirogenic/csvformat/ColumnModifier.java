package com.epeirogenic.csvformat;

/**
 * Created by philipcoates on 29/12/2015.
 */
public interface ColumnModifier {

    void modifyColumn(int columnNumber) throws Exception;

    String transformValue(String source) throws Exception;
}
