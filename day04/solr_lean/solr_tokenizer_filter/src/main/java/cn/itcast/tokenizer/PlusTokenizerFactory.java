package cn.itcast.tokenizer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

public class PlusTokenizerFactory extends TokenizerFactory {
    /**
     *   <analyzer>
     *      *         <tokenizer class="cn.itcast.tokenizer.PlausSignTokenizerFactory" mode="complex"/>
     *   </analyzer>
     * @param args
     */
    public PlusTokenizerFactory(Map<String, String> args) {
        super(args);
    }

    public Tokenizer create(AttributeFactory attributeFactory) {
        return new PlusTokenizer();
    }
}
