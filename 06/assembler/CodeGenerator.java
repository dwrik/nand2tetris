import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class CodeGenerator {

    private int unallocatedPointer = 16;

    private final List<String[]> parsedLines;
    private final Map<String, Integer> symbolTable;
    private final Map<String, String> destTable;
    private final Map<String, String> compTable;
    private final Map<String, String> jumpTable;

    private final int WORD_SIZE = 16;

    // initialize code generator
    public CodeGenerator(Map<String, Integer> symbolTable, List<String[]> parsedLines) {
        this.parsedLines = parsedLines;
        this.symbolTable = symbolTable;
        this.destTable = new HashMap<>();
        this.compTable = new HashMap<>();
        this.jumpTable = new HashMap<>();
        populateOpcodes();
    }

    // return code generated for line of source code
    public List<String> getGeneratedCode() {
        List<String> generatedCode = new ArrayList<>();
        for (String[] line : parsedLines) {
            generatedCode.add(generateCode(line));
        }
        return generatedCode;
    }

    // generate code for a single line/instruction
    private String generateCode(String[] instruction) {
        // constants for instruction fields
        final int TYPE = 0, ADDR = 1;
        final int DEST = 1, COMP = 2, JUMP = 3;

        // mutable string for building up code
        StringBuilder line = new StringBuilder();

        switch (instruction[TYPE]) {
            case "A":
                // add instruction bit (1 bit)
                line.append(0);

                // resolve label/address
                int address = resolveAddress(instruction[ADDR]);

                // convert address to binary
                String binary = Integer.toBinaryString(address);

                // add padding bits (0's to fulfill word size)
                while (line.length() + binary.length() < WORD_SIZE) {
                    line.append(0);
                }

                // add address bits
                line.append(binary);
                break;

            case "C":
                // add instruction bit (1 bit)
                line.append(1);

                // add padding bits (2 bit)
                line.append("11");

                // add opcode bits
                line.append(compTable.get(instruction[COMP])); // comp (7 bits)
                line.append(destTable.get(instruction[DEST])); // dest (3 bits)
                line.append(jumpTable.get(instruction[JUMP])); // jump (3 bits)
                break;

            default:
                HackAssembler.exit("Error: invalid instruction in file", 3);
        }

        return line.toString();
    }

    // resolves a given address string to a numeric
    // address, existing symbol or allocated variable
    private int resolveAddress(String identifier) {
        int address = -1;
        if (symbolTable.containsKey(identifier)) {
            address = symbolTable.get(identifier);
        } else {
            try {
                address = Integer.parseInt(identifier);
            } catch (NumberFormatException e) {
                address = unallocatedPointer++;
                symbolTable.put(identifier, address);
            }
        }
        return address;
    }

    // map opcodes to their binary
    private void populateOpcodes() {
        destTable.put(null, "000");
        destTable.put("M", "001");
        destTable.put("D", "010");
        destTable.put("MD", "011");
        destTable.put("A", "100");
        destTable.put("AM", "101");
        destTable.put("AD", "110");
        destTable.put("AMD", "111");

        jumpTable.put(null, "000");
        jumpTable.put("JGT", "001");
        jumpTable.put("JEQ", "010");
        jumpTable.put("JGE", "011");
        jumpTable.put("JLT", "100");
        jumpTable.put("JNE", "101");
        jumpTable.put("JLE", "110");
        jumpTable.put("JMP", "111");

        compTable.put("0", "0101010");
        compTable.put("1", "0111111");
        compTable.put("-1", "0111010");
        compTable.put("D", "0001100");
        compTable.put("A", "0110000");
        compTable.put("!D", "0001101");
        compTable.put("!A", "0110001");
        compTable.put("-D", "0001111");
        compTable.put("-A", "0110011");
        compTable.put("D+1", "0011111");
        compTable.put("A+1", "0110111");
        compTable.put("D-1", "0001110");
        compTable.put("A-1", "0110010");
        compTable.put("D+A", "0000010");
        compTable.put("D-A", "0010011");
        compTable.put("A-D", "0000111");
        compTable.put("D&A", "0000000");
        compTable.put("D|A", "0010101");

        compTable.put("M", "1110000");
        compTable.put("!M", "1110001");
        compTable.put("-M", "1110011");
        compTable.put("M+1", "1110111");
        compTable.put("M-1", "1110010");
        compTable.put("D+M", "1000010");
        compTable.put("D-M", "1010011");
        compTable.put("M-D", "1000111");
        compTable.put("D&M", "1000000");
        compTable.put("D|M", "1010101");
    }
}
