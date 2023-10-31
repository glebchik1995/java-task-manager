package enums;

import com.google.gson.annotations.SerializedName;

public enum Status {
    @SerializedName("NEW")
    NEW,
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS,
    @SerializedName("DONE")
    DONE
}
