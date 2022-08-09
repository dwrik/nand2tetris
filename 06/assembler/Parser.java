import java.util.List;
import java.util.ArrayList;

public class Parser {

    private final List<String> lines;

    // initialize parser with valid source code
    // lines with comments and whitespaces removed
    public Parser(List<String> lines) {
        this.lines = lines;
    }

    // parse each line and return all parsed lines
    public List<String[]> getParsedLines() {
        List<String[]> parsedLines = new ArrayList<>();
        for (String line : lines) {
            parsedLines.add(parseLine(line));
        }
        return parsedLines;
    }

    // parse a single valid source code line and
    // return String[] based on type of instruction:
    // A-instruction: ["A", addr]
    // C-instruction: ["C", dest, comp, jump]
    private String[] parseLine(String line) {
        String[] parsedLine = null;
        if (line.charAt(0) == '@') { // parse A-instruction
            parsedLine = new String[] { "A", line.substring(1) };
        } else { // parse C-instruction
            String[] p = line.split("[=;]");
            if (p.length == 3) {
                parsedLine = new String[] { "C", p[0], p[1], p[2] }; // dest=comp;jump
            } else if (p[1].charAt(0) == 'J') {
                parsedLine = new String[] { "C", null, p[0], p[1] }; // comp;jump
            } else {
                parsedLine = new String[] { "C", p[0], p[1], null }; // dest=comp
            }
        }
        return parsedLine;
    }
}
