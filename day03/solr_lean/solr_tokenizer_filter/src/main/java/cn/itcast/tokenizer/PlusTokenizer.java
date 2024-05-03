package cn.itcast.tokenizer;

import org.apache.lucene.analysis.util.CharTokenizer;

public class PlusTokenizer extends CharTokenizer {
    protected boolean isTokenChar(int i) {
        return i != '+';
    }
}
