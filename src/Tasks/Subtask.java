package Tasks;

import EnumTasks.Status;
import EnumTasks.Types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(int id, String title, Status status, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, title, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String description, String name, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(description, name, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return getId() + "," + Types.SUBTASK + "," +
                getTitle() + "," + getStatus() + "," + getDescription() + "," +
                getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) + "," +
                getEndTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) + "," +
                getDuration().toMinutes() + "," + getEpicId();
    }

}
