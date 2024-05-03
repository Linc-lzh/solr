package cn.itcast.analyzer;

import cn.itcast.filter.KeywordsFilter;
import cn.itcast.tokenizer.PlusTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public class PlusAnalyzer extends Analyzer {
    /**
     * 分词器和过滤器封装在createComponents()方法的返回值即可
     * @param fieldName
     * @return
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        //1.创建分词器
        Tokenizer tokenizer = new PlusTokenizer();
        //2.创建过滤器
        KeywordsFilter filter = new KeywordsFilter(tokenizer, "like");

        //3.封装TokenStreamComponents中
        return new TokenStreamComponents(tokenizer, filter);
    }
}
