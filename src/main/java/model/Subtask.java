package model;

import enums.Status;
import enums.Types;

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

    public Subtask(String description, String title, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(description, title, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, LocalDateTime startTime, Duration duration, int epicId) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, Types types, String title, Status status, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, types, title, status, description, startTime, duration);
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
        return getId() +
                "," + getType() +
                "," + getTitle() +
                "," + getStatus() +
                "," + getDescription() +
                "," + getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) +
                "," + getDuration().toMinutes() +
                "," + epicId;
    }

}
