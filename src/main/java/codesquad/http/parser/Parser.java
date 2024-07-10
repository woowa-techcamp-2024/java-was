package codesquad.http.parser;

public interface Parser<T> {

    T parse(String text);

}
