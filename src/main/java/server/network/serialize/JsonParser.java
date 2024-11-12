package server.network.serialize;

public class JsonParser {
    private final String json;
    private int position = 0;

    public JsonParser(String json) {
        this.json = json;
    }

    public char peek() {
        if (position >= json.length()) {
            throw new IllegalStateException("Unexpected end of input");
        }
        return json.charAt(position);
    }

    public char next() {
        return json.charAt(position++);
    }

    public void expect(char expected) {
        char actual = next();
        if (actual != expected) {
            throw new IllegalStateException("Expected " + expected + " but got " + actual);
        }
    }

    public void skipWhitespace() {
        while (position < json.length() && Character.isWhitespace(json.charAt(position))) {
            position++;
        }
    }

    public String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        boolean escaped = false;

        while (position < json.length()) {
            char c = next();
            if (escaped) {
                switch (c) {
                    case '"':
                    case '\\':
                    case '/':
                        sb.append(c);
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    default:
                        throw new IllegalStateException("Invalid escape sequence: \\" + c);
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public Object parseNumber() {
        StringBuilder sb = new StringBuilder();
        boolean isDecimal = false;

        if (peek() == '-') {
            sb.append(next());
        }

        while (position < json.length()) {
            char c = peek();
            if (Character.isDigit(c)) {
                sb.append(next());
            } else if (c == '.') {
                isDecimal = true;
                sb.append(next());
            } else if (c == 'e' || c == 'E') {
                isDecimal = true;
                sb.append(next());
                if (peek() == '+' || peek() == '-') {
                    sb.append(next());
                }
            } else {
                break;
            }
        }

        String numStr = sb.toString();
        if (isDecimal) {
            return Double.parseDouble(numStr);
        } else {
            long value = Long.parseLong(numStr);
            if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                return (int) value;
            }
            return value;
        }
    }

    public boolean parseBoolean() {
        if (peek() == 't') {
            position += 4; // "true"
            return true;
        } else if (peek() == 'f') {
            position += 5; // "false"
            return false;
        }
        throw new IllegalStateException("Invalid boolean value");
    }

    public void parseNull() {
        if (peek() == 'n') {
            position += 4; // "null"
        } else {
            throw new IllegalStateException("Invalid null value");
        }
    }
}
