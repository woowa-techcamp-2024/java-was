package codesquad.utils.json;

import java.util.List;

public class JsonMapper {

    public static <T> String listToJson(List<T> target) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < target.size(); i++) {
            sb.append(target.get(i).toString());
            if (i < target.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
