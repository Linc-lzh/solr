package cn.itcast.filter;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

public class KeywordsTokenFilterFactory extends TokenFilterFactory {
    private String keywords = "love";
    /**
     * 过滤器参数。
     * @param args
     */
    public KeywordsTokenFilterFactory(Map<String, String> args) {
        super(args);
        if(args.containsKey("keywords")) {
            this.keywords = args.get("keywords");
        }
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new KeywordsFilter(tokenStream, keywords);
    }
}
