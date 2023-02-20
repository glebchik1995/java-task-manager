package model;

import manager.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subtasksId;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        this.subtasksId = new ArrayList<>();
    }

    public Epic(int id, String title, Status status, String description) {
        super(id, title, status, description);
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksId, epic.subtasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksId);
    }

    @Override
    public String toString() {
        return getId() +
                "," + Types.EPIC + "," +
                getTitle() + "," + getStatus() + "," + getDescription();
    }

}
