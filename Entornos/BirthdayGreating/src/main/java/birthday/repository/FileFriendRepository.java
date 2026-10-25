package birthday.repository;

import birthday.model.Friend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileFriendRepository implements FriendRepository {

    private final Path file;

    public FileFriendRepository(Path file) {
        this.file = file;
    }

    @Override
    public List<Friend> findAll() {
        List<String> lines;
        try {
            lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RepositoryException("No se puede leer el fichero " + file, e);
        }
        List<Friend> friends = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).strip();
            if (line.isEmpty() || (i == 0 && line.startsWith("last_name"))) {
                continue;
            }
            friends.add(FriendParser.parse(line));
        }
        return friends;
    }
}
