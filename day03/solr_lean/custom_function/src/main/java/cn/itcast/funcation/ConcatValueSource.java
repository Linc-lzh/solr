package cn.itcast.funcation;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.StrDocValues;

import java.io.IOException;
import java.util.Map;

public class ConcatValueSource extends ValueSource {

    private ValueSource valueSource1;
    private ValueSource valueSource2;

    public ConcatValueSource(ValueSource valueSource1, ValueSource valueSource2) {
        this.valueSource1 = valueSource1;
        this.valueSource2 = valueSource2;
    }

    @Override
    public FunctionValues getValues(Map map, LeafReaderContext leafReaderContext) throws IOException {
        //1.获取形参值
        FunctionValues arg1 = valueSource1.getValues(map, leafReaderContext); //域名
        FunctionValues arg2 = valueSource2.getValues(map, leafReaderContext); //域名
        return new StrDocValues(this) {
            @Override
            public String strVal(int doc) throws IOException {
                return arg1.strVal(doc) + arg2.strVal(doc);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String description() {
        return null;
    }
}
