class Enigma {
    private class PlugBoard {
        private char[] plugBoardArray;

        public PlugBoard() {
            plugBoardArray = new char[26];
            for (int i = 0; i < 26; i++) {
                plugBoardArray[i] = (char) (i + 'A');
            }
        }

        public PlugBoard(PlugBoard plugBoard) {
            // plugBoardArray is not const, so make a clone
            plugBoardArray = plugBoard.plugBoardArray.clone();
        }

        public char runThrough(char input) {
            return plugBoardArray[input - 'A'];
        }

        public void setPlugs(char port1, char port2) {
            if (plugBoardArray[port1 - 'A'] != port1) {
                // already set, remove existing connection
                removeConnectionPair(port1);

            }
            if (plugBoardArray[port2 - 'A'] != port2) {
                // already set, remove existing connection
                removeConnectionPair(port2);
            }

            plugBoardArray[port1 - 'A'] = port2;
            plugBoardArray[port2 - 'A'] = port1;
        }

        private void removeConnectionPair(char port1) {
            for (int i = 0; i < plugBoardArray.length; i++) {
                if (plugBoardArray[i] == port1) {
                    plugBoardArray[i] = (char) (i + 'A');
                }
            }
        }
    }

    enum RotorType {
        I, II, III, IV, V
    }

    private class Rotor {
        private RotorType rotorType;
        private String rotorArray;
        private char rotorPosition;

        public Rotor(RotorType rotorType, char startPosition) {
            switch (rotorType) {
                case I:
                    rotorArray = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
                    break;
                case II:
                    rotorArray = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
                    break;
                case III:
                    rotorArray = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
                    break;
                case IV:
                    rotorArray = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
                    break;
                case V:
                    rotorArray = "VZBRGITYUPSDNHLXAWMJQOFECK";
            }

            rotorPosition = startPosition;
            this.rotorType = rotorType;
        }

        public Rotor(Rotor rotor) {
            // all values are const or primative, so no need to clone
            rotorType = rotor.rotorType;
            rotorArray = rotor.rotorArray;
            rotorPosition = rotor.rotorPosition;
        }

        public char runThroughForward(char input) {
            char inLetter = (char) ((input + rotorPosition - 2 * 'A') % 26 + 'A');
            char outLetter = rotorArray.charAt(inLetter - 'A');
            return (char) (Math.floorMod((outLetter - rotorPosition), 26) + 'A');
        }

        public char runThroughBackward(char input) {
            char inLetter = (char) ((input + rotorPosition - 2 * 'A') % 26 + 'A');
            char outLetter = (char) (rotorArray.indexOf(inLetter) + 'A');
            return (char) (Math.floorMod((outLetter - rotorPosition), 26) + 'A');
        }

        public boolean rotate() {
            rotorPosition = (rotorPosition == 'Z') ? 'A' : (char) (rotorPosition + 1);

            switch (rotorType) {
                case I:
                    if (rotorPosition == 'R') {
                        return true;
                    }
                    break;
                case II:
                    if (rotorPosition == 'F') {
                        return true;
                    }
                    break;
                case III:
                    if (rotorPosition == 'W') {
                        return true;
                    }
                    break;
                case IV:
                    if (rotorPosition == 'K') {
                        return true;
                    }
                    break;
                case V:
                    if (rotorPosition == 'A') {
                        return true;
                    }
            }

            return false;
        }
    }

    enum ReflectorType {
        A, B, C
    }

    private class Reflector {
        private String reflectorArray;

        public Reflector(ReflectorType reflectorType) {
            switch (reflectorType) {
                case A:
                    reflectorArray = "EJMZALYXVBWFCRQUONTSPIKHGD";
                    break;
                case B:
                    reflectorArray = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
                    break;
                case C:
                    reflectorArray = "FVPJIAOYEDRZXWGCTKUQSBNMHL";
            }
        }

        public char runThrough(char input) {
            return reflectorArray.charAt(input - 'A');
        }
    }

    private PlugBoard plugBoard;
    private Rotor rotor1;
    private Rotor rotor2;
    private Rotor rotor3;
    private Reflector reflector;

    public Enigma(RotorType rotor1Type, RotorType rotor2Type, RotorType rotor3Type, ReflectorType reflectorType,
            char startPosition1, char startPosition2, char startPosition3) throws IllegalArgumentException {
        startPosition1 = Character.toUpperCase(startPosition1);
        startPosition2 = Character.toUpperCase(startPosition2);
        startPosition3 = Character.toUpperCase(startPosition3);

        if (startPosition1 < 'A' || startPosition1 > 'Z' || startPosition2 < 'A' || startPosition2 > 'Z'
                || startPosition3 < 'A' || startPosition3 > 'Z') {
            throw new IllegalArgumentException("Start positions must be between A and Z");
        }

        plugBoard = new PlugBoard();
        rotor1 = new Rotor(rotor1Type, startPosition1);
        rotor2 = new Rotor(rotor2Type, startPosition2);
        rotor3 = new Rotor(rotor3Type, startPosition3);
        reflector = new Reflector(reflectorType);
    }

    public Enigma(Enigma other) {
        // reflectors are const, so no need to use copy constructor
        this.plugBoard = new PlugBoard(other.plugBoard);
        this.rotor1 = new Rotor(other.rotor1);
        this.rotor2 = new Rotor(other.rotor2);
        this.rotor3 = new Rotor(other.rotor3);
        this.reflector = other.reflector;
    }

    public void setPlugs(char port1, char port2) throws IllegalArgumentException {
        port1 = Character.toUpperCase(port1);
        port2 = Character.toUpperCase(port2);

        if (port1 < 'A' || port1 > 'Z' || port2 < 'A' || port2 > 'Z') {
            throw new IllegalArgumentException("Ports must be between A and Z");
        }

        plugBoard.setPlugs(port1, port2);
    }

    public char encryptChar(char toEncrypt) throws IllegalArgumentException {
        toEncrypt = Character.toUpperCase(toEncrypt);

        if (toEncrypt < 'A' || toEncrypt > 'Z') {
            throw new IllegalArgumentException("Characters must be between A and Z");
        }

        // rotate rotors
        if (rotor1.rotate()) {
            if (rotor2.rotate()) {
                rotor3.rotate();
            }
        }

        // run through
        char temp = plugBoard.runThrough(toEncrypt);
        temp = rotor1.runThroughForward(temp);
        temp = rotor2.runThroughForward(temp);
        temp = rotor3.runThroughForward(temp);
        temp = reflector.runThrough(temp);
        temp = rotor3.runThroughBackward(temp);
        temp = rotor2.runThroughBackward(temp);
        temp = rotor1.runThroughBackward(temp);
        return plugBoard.runThrough(temp);
    }

    public String encryptString(String toEncrypt) throws IllegalArgumentException {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < toEncrypt.length(); i++) {
            res.append(encryptChar(toEncrypt.charAt(i)));
        }

        return res.toString();
    }
}

public class App {
    public static void main(String[] args) {
        Enigma enigma1 = new Enigma(Enigma.RotorType.I, Enigma.RotorType.II, Enigma.RotorType.III,
                Enigma.ReflectorType.A, 'A', 'A', 'A');

        enigma1.setPlugs('A', 'B');
        enigma1.setPlugs('C', 'D');

        Enigma enigma2 = new Enigma(enigma1);

        String toEncrypt = "HELLOWORLD";
        String encrypted = enigma1.encryptString(toEncrypt);
        String decrypted = enigma2.encryptString(encrypted);

        System.out.println((toEncrypt.equals(decrypted)) ? "Encryption and decryption are the same"
                : "Encryption and decryption are not the same");
        System.out.println(encrypted);
    }
}
