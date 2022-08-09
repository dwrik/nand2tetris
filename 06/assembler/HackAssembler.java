import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class HackAssembler {

    // symbol table
    private static final Map<String, Integer> symbolTable = new HashMap<>();

    // source code lines
    private static final List<String> lines = new ArrayList<>();

    // run assembler
    public static void main(String[] args) {
        // exit with status code 5 if file not provided
        if (args.length == 0) {
            exit("Usage: java HackAssembler <source-file>", 5);
        }

        // initialize symbolTable
        populatePredefinedSymbols();

        // read and pre-process file
        readFileAndPopulate(args[0]);

        // initialize parser and parse lines
        Parser parser = new Parser(lines);
        List<String[]> parsedLines = parser.getParsedLines();

        // initialize code generator and generate code
        CodeGenerator codegen = new CodeGenerator(symbolTable, parsedLines);
        List<String> generatedLines = codegen.getGeneratedCode();

        // write generated code to .hack file
        writeToFile(args[0], generatedLines);
    }

    // write generated code to file as text
    private static void writeToFile(String fileName, List<String> contents) {
        // replace .asm extension with .hack
        String assembledFileName = fileName.replaceAll(".asm$", ".hack");
        // write assembled code to file
        try (PrintWriter printWriter = new PrintWriter(assembledFileName)) {
            for (String line : contents) {
                printWriter.println(line);
            }
        } catch (Exception e) {
            // exit with status code 9 if file cannot be created
            exit("Error: failed to create assembled .hack file", 9);
        }
    }

    // read and populate 'lines' and 'symbolTable' based on source code lines
    private static void readFileAndPopulate(String fileName) {
        // read source file and load to memory
        try (Scanner sc = new Scanner(new File(fileName))) {
            // line number for keeping track of jumps
            int lineNumber = 0;
            // read lines till EOF
            while (sc.hasNext()) {
                // remove comments (line/inline) and strip leading/trailing whitespace
                String line = sc.nextLine().replaceAll("//.*", "").strip();
                // check if label declaration
                boolean isLabelDeclaration = line.startsWith("(");
                // skip line if blank or label
                if (line.isBlank() || isLabelDeclaration) {
                    // add to symbol table if label
                    if (isLabelDeclaration) {
                        // remove parentheses around label name before adding
                        symbolTable.put(line.split("[\\(\\)]")[1], lineNumber);
                    }
                } else {
                    // add line list if valid line
                    lines.add(line);
                    // increment line number
                    lineNumber++;
                }
            }
        } catch (FileNotFoundException e) {
            // exit with status code 4 if file not found
            exit(String.format("Error: %s does not exist", fileName), 4);
        }
    }

    // print error message and exit with status code
    public static void exit(String message, int status) {
        System.err.println(message);
        System.exit(status);
    }

    // add predefined symbols in the symbol table
    private static void populatePredefinedSymbols() {
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
    }
}
