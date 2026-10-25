package birthday;

import birthday.model.Friend;
import birthday.notifier.ConsoleNotifier;
import birthday.notifier.EmailNotifier;
import birthday.notifier.Notifier;
import birthday.notifier.SmsNotifier;
import birthday.repository.FileFriendRepository;
import birthday.repository.FriendRepository;
import birthday.repository.SqliteFriendRepository;
import birthday.service.BirthdayService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/*
 * Uso:
 *   --source=file|sqlite    origen de datos (por defecto file)
 *   --file=friends.txt      fichero de amigos
 *   --db=friends.db         base de datos SQLite
 *   --import                vuelca el fichero de texto a la base de datos SQLite
 *   --notifier=console|email|sms   método de envío (por defecto console)
 *   --date=2026-10-08       simula otra fecha (por defecto hoy)
 *   --no-reminders          desactiva los recordatorios al resto de amigos
 */
public class Main {

    public static void main(String[] args) {
        Map<String, String> opts = parseArgs(args);

        Path file = Path.of(opts.getOrDefault("file", "friends.txt"));
        String db = opts.getOrDefault("db", "friends.db");

        if (opts.containsKey("import")) {
            SqliteFriendRepository sqlite = new SqliteFriendRepository(db);
            sqlite.deleteAll();
            for (Friend f : new FileFriendRepository(file).findAll()) {
                sqlite.save(f);
            }
            System.out.println("Importados " + sqlite.findAll().size() + " amigos en " + db);
        }

        FriendRepository repository = switch (opts.getOrDefault("source", "file")) {
            case "file" -> new FileFriendRepository(file);
            case "sqlite" -> new SqliteFriendRepository(db);
            default -> throw new IllegalArgumentException("Origen no soportado: " + opts.get("source"));
        };

        Notifier notifier = switch (opts.getOrDefault("notifier", "console")) {
            case "console" -> new ConsoleNotifier();
            case "sms" -> new SmsNotifier();
            case "email" -> createEmailNotifier();
            default -> throw new IllegalArgumentException("Método de envío no soportado: " + opts.get("notifier"));
        };

        LocalDate today = opts.containsKey("date") ? LocalDate.parse(opts.get("date")) : LocalDate.now();
        boolean reminders = !opts.containsKey("no-reminders");

        System.out.println("Fecha: " + today + " | Origen: " + repository.getClass().getSimpleName()
                + " | Envío: " + notifier.getClass().getSimpleName());

        BirthdayService service = new BirthdayService(repository, notifier, reminders);
        BirthdayService.Result result = service.sendGreetings(today);

        System.out.println("----------------------------------------");
        System.out.println(result);
    }

    private static Notifier createEmailNotifier() {
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(Path.of("config.properties"))) {
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Falta config.properties (copia config.properties.example)", e);
        }
        return new EmailNotifier(
                p.getProperty("smtp.host"),
                Integer.parseInt(p.getProperty("smtp.port", "587")),
                p.getProperty("smtp.user"),
                p.getProperty("smtp.password"),
                p.getProperty("smtp.from", p.getProperty("smtp.user")));
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> opts = new HashMap<>();
        for (String arg : args) {
            if (!arg.startsWith("--")) continue;
            String[] kv = arg.substring(2).split("=", 2);
            opts.put(kv[0], kv.length > 1 ? kv[1] : "true");
        }
        return opts;
    }
}
