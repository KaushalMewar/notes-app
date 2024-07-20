package io.km.notes.services;

import io.km.notes.models.ErrorResponse;
import io.km.notes.models.Note;
import io.km.notes.models.NoteResponse;
import io.km.notes.models.SuccessResponse;
import io.km.notes.repositorys.NotesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NotesService {

    private static final Logger logger = LoggerFactory.getLogger(NotesService.class);

    private final NotesRepository notesRepository;

    @Autowired
    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    /**
     * Retrieves all notes from the repository, reverses the list, and returns it.
     * Logs the entry and exit points along with the number of notes retrieved.
     *
     * @return SuccessResponse containing the list of notes
     */
    public SuccessResponse<List<Note>> getAllNotes() {
        logger.info("Entering getAllNotes method.");
        List<Note> notes = notesRepository.findAll();
        Collections.reverse(notes);  // Reverse the list to show the most recent notes first
        logger.info("Retrieved {} notes from the database.", notes.size());
        return new SuccessResponse<>(notes);
    }

    /**
     * Saves a new note to the repository after validating its description.
     * Logs the entry, success, and failure scenarios, including exceptions.
     *
     * @param note Note object to be saved
     * @return SuccessResponse with the saved note or ErrorResponse in case of validation or other errors
     */
    public Object saveNote(Note note) {
        logger.info("Entering saveNote method with note: {}", note);
        try {
            NoteResponse validationResponse = validateNoteDescription(note.getDescription());
            if (validationResponse == null) {
                note.setDateTime(LocalDateTime.now());
                Note savedNote = notesRepository.save(note);
                logger.info("Note saved successfully with ID: {}", savedNote.getId());
                return new SuccessResponse<>(savedNote);
            } else {
                logger.warn("Validation failed: {}", validationResponse.getErrorMessage());
                return createErrorResponse(HttpStatus.BAD_REQUEST, validationResponse.getErrorMessage());
            }
        } catch (IllegalArgumentException e) {
            String errorMessage = "Validation error occurred while saving note: " + e.getMessage();
            logger.error("Exception in saveNote method: {}", errorMessage, e);
            return createErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
        } catch (Exception e) {
            String errorMessage = "Unexpected error occurred while saving note: " + e.getMessage();
            logger.error("Exception in saveNote method: {}", errorMessage, e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    /**
     * Validates the description of a note to ensure it is not null or empty.
     *
     * @param description The description of the note to validate
     * @return NoteResponse with an error message if validation fails; otherwise, null
     */
    private NoteResponse validateNoteDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return new NoteResponse("Validation error: Description is 'Null/Empty'");
        }
        return null;
    }

    /**
     * Retrieves a specific note by its ID from the repository.
     * Logs the entry and whether the note was found or not.
     *
     * @param id The ID of the note to retrieve
     * @return SuccessResponse with the note or ErrorResponse if the note is not found
     */
    public Object getANote(String id) {
        logger.info("Entering getANote method with ID: {}", id);
        try {
            Optional<Note> note = notesRepository.findById(id);
            if (note.isPresent()) {
                logger.info("Note found with ID: {}", id);
                return new SuccessResponse<>(note.get());
            } else {
                logger.warn("No note found for ID: {}", id);
                return createErrorResponse(HttpStatus.BAD_REQUEST, "No note found for id -> " + id);
            }
        } catch (Exception e) {
            String errorMessage = "Unexpected error occurred while retrieving note with ID: " + id;
            logger.error("Exception in getANote method: {}", errorMessage, e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    /**
     * Updates an existing note in the repository.
     * Logs the entry and success scenarios, including exceptions.
     *
     * @param note Note object with updated details
     * @return SuccessResponse with the updated note or ErrorResponse in case of an error
     */
    public Object updateNote(Note note) {
        logger.info("Entering updateNote method with note: {}", note);
        try {
            Note updatedNote = notesRepository.save(note);
            logger.info("Note updated successfully with ID: {}", updatedNote.getId());
            return new SuccessResponse<>(updatedNote);
        } catch (Exception e) {
            String errorMessage = "Unexpected error occurred while updating note: " + e.getMessage();
            logger.error("Exception in updateNote method: {}", errorMessage, e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    /**
     * Deletes a note by its ID from the repository.
     * Logs the entry and success scenarios, including exceptions.
     *
     * @param id The ID of the note to delete
     * @return SuccessResponse with a confirmation message or ErrorResponse in case of an error
     */
    public Object deleteNote(String id) {
        logger.info("Entering deleteNote method with ID: {}", id);
        try {
            notesRepository.deleteById(id);
            logger.info("Note with ID: {} successfully deleted.", id);
            return new SuccessResponse<>(String.format("Note with id -> %s successfully deleted.", id));
        } catch (Exception e) {
            String errorMessage = "Unexpected error occurred while deleting note with ID: " + id;
            logger.error("Exception in deleteNote method: {}", errorMessage, e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    /**
     * Creates an error response with a specific status code and message.
     * Logs the details of the error response creation.
     *
     * @param status The HTTP status associated with the error
     * @param errorMessage The detailed error message
     * @return ErrorResponse containing error details
     */
    private ErrorResponse createErrorResponse(HttpStatus status, String errorMessage) {
        String errorCode;
        String errorTitle = switch (status) {
            case BAD_REQUEST -> {
                errorCode = "400_BAD_REQUEST";
                yield "Invalid Request";
            }
            case NOT_FOUND -> {
                errorCode = "404_NOT_FOUND";
                yield "Resource Not Found";
            }
            case INTERNAL_SERVER_ERROR -> {
                errorCode = "500_INTERNAL_ERROR";
                yield "Server Error";
            }
            default -> {
                errorCode = "UNKNOWN_ERROR";
                yield "Unknown Error";
            }
        };

        // Determine error code and title based on HTTP status

        ErrorResponse.Error error = new ErrorResponse.Error(errorCode, errorTitle, errorMessage);
        logger.error("Creating error response: Code={}, Title={}, Message={}", errorCode, errorTitle, errorMessage);
        return new ErrorResponse(Collections.singletonList(error));
    }
}
