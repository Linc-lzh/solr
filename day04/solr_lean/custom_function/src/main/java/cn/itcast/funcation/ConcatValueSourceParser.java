package cn.itcast.funcation;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

/**
 * 用来解析函数调用语法，并且可以解析参数类型，以及返回指定结果
 * concat(field1,field2)
 */
public class ConcatValueSourceParser extends ValueSourceParser {
    @Override
    public ValueSource parse(FunctionQParser fp) throws SyntaxError {
        //获取形参类型
        ValueSource valueSource1 = fp.parseValueSource();
        ValueSource valueSource2 = fp.parseValueSource();
        //返回函数执行结果
        return new ConcatValueSource(valueSource1, valueSource2);
    }
}
