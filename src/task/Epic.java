package task;

import enumTask.Status;
import enumTask.Types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String title, Status status, String description, LocalDateTime startTime, Duration duration) {
        super(id, title, status, description, startTime, duration);
    }

    public Epic(String description, String title, Status status, LocalDateTime startTime, Duration duration) {
        super(description, title, status, startTime, duration);
        this.endTime = super.getEndTime();
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(int id) {
        subtasksId.add(id);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksId, epic.subtasksId) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksId, endTime);
    }

    @Override
    public String toString() {
        return getId() + "," + Types.EPIC + "," +
                getTitle() + "," + getStatus() + "," + getDescription() + "," +
                getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) + "," +
                getEndTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) + "," +
                getDuration().toMinutes();
    }

}
