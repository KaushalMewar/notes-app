package io.km.notes.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteResponse {

    private Note note;
    private String errorMessage;

    public NoteResponse(Note note) {
        this.note = note;
    }

    public NoteResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return note != null;
    }
}
