package pe.edu.pucp.empresasag.auxiliar;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {

        if(stringList!= null && stringList.size()==0) return "";
        return stringList != null ? String.join(SPLIT_CHAR, stringList) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        if (string.length()==0) return new ArrayList<>();
        return string != null ? Arrays.asList(string.split(SPLIT_CHAR)) : new ArrayList<>();
    }
}
