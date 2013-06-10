/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.writer.customHandler;

import coffeepot.bean.wr.typeHandler.DefaultDoubleHandler;
import java.text.DecimalFormat;

/**
 * Only tests.
 * @author Jeandeson O. Merelis
 */
public class CustomDoubleHandler extends DefaultDoubleHandler {

    
    @Override
    public void setConfig(String[] params) {
        setDefaultValues();
    }

    
    
    private void setDefaultValues() {
        pattern = "#,##0.000";
        decimalFormat = new DecimalFormat(pattern);
        decimalSeparator = decimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
        groupingSeparator = decimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
    }
}
