package model;

import java.util.Objects;

public class Subtask extends Task {
    final private int epicId;

    public Subtask(String title, String description, String status, int epicId) {
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
        return "Subtask{" +
                " title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
