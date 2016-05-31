package ua.nure.nikolaienko;

/**
 * Created by Vladyslav_Nikolaienk on 5/31/2016.
 */
public class TODO {

    String value;
    Long lineNumber;
    String fileName;

    public TODO(String value, Long lineNumber, String fileName) {
        this.value = value;
        this.lineNumber = lineNumber;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TODO{");
        sb.append("value='").append(value).append('\'');
        sb.append(", lineNumber=").append(lineNumber);
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
