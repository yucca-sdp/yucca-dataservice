/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package it.csi.phoenix.expression.function;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;

import org.apache.phoenix.expression.Expression;
import org.apache.phoenix.expression.function.ScalarFunction;
import org.apache.phoenix.schema.types.PDataType;
import org.apache.phoenix.schema.types.PDataType.PDataCodec;

import it.csi.phoenix.util.DateUtil;

public abstract class DateScalarFunction extends ScalarFunction {
    protected PDataCodec inputCodec;

    public DateScalarFunction() {
    }

    public DateScalarFunction(List<Expression> children) {
        super(children);
        init();
    }

    protected final PDataCodec getInputCodec() {
        return inputCodec;
    }
    
    @Override
    public void readFields(DataInput input) throws IOException {
        super.readFields(input);
        init();
    }
    
    private void init() {
        PDataType returnType = getChildren().get(0).getDataType();
        inputCodec = DateUtil.getCodecFor(returnType);
    }
}
