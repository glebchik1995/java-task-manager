package model;

public class Epic extends Task{

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }

    @Override
    public String toString() {
        return "Epic{" +
                        ", title='" + title + '\'' +
                        ", description='" + description + '\'' +
                        ", id=" + id +
                        ", status='" + status + '\'' +
                        '}';
    }
}

