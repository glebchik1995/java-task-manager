package model;

import manager.Types;

import java.util.Objects;
public class Subtask extends Task {
    private final int epicId;

    public Subtask(int id, String title, Status status, String description, int epicId) {
        super(id, title,status, description);
        this.epicId = epicId;
    }
    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
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
                getTitle() + "," + getStatus() + "," + getDescription() + "," + getEpicId();
    }

}
