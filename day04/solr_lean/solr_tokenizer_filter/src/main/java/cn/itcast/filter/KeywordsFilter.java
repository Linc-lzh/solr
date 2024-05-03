package cn.itcast.filter;

import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

public class KeywordsFilter extends FilteringTokenFilter {

    //接收过滤词
    private String keywords;
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    /**
     * Create a new {@link FilteringTokenFilter}.
     *
     * @param in the {@link TokenStream} to consume
     */
    public KeywordsFilter(TokenStream in,String keywords) {
        super(in);
        this.keywords = keywords;
    }

    /**
     * true：保留词
     * false:过滤
     * @return
     * @throws IOException
     */
    @Override
    protected boolean accept() throws IOException {
        if(termAtt.toString().equals(keywords)) {
            return false;
        }
        return true;

    }
}
