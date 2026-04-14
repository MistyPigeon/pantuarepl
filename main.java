import jdk.jshell.Diag;
import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;

import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        System.out.println("Java REPL (JShell-backed)");
        System.out.println("Type Java code (expressions, statements, methods, classes).");
        System.out.println("Commands: :help, :exit");

        try (JShell jshell = JShell.create(); Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("java> ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }

                if (":exit".equalsIgnoreCase(input) || ":quit".equalsIgnoreCase(input)) {
                    System.out.println("Bye!");
                    break;
                }

                if (":help".equalsIgnoreCase(input)) {
                    printHelp();
                    continue;
                }

                List<SnippetEvent> events = jshell.eval(input);
                for (SnippetEvent event : events) {
                    if (event.exception() != null) {
                        System.out.println("Exception: " + event.exception().getMessage());
                        continue;
                    }

                    if (event.status() == jdk.jshell.Snippet.Status.REJECTED) {
                        for (Diag diag : jshell.diagnostics(event.snippet()).toList()) {
                            System.out.println("Error: " + diag.getMessage(null));
                        }
                        continue;
                    }

                    if (event.value() != null) {
                        System.out.println(event.value());
                    }
                }
            }
        }
    }

    private static void printHelp() {
        System.out.println("Enter valid Java code, for example:");
        System.out.println("  int x = 10;");
        System.out.println("  x + 5");
        System.out.println("  String hi(String n) { return \"Hi \" + n; }");
        System.out.println("  hi(\"Sam\")");
        System.out.println("Commands:");
        System.out.println("  :help - show this message");
        System.out.println("  :exit - quit the REPL");
    }
}
