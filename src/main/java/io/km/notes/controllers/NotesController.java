package io.km.notes.controllers;

import io.km.notes.models.Note;
import io.km.notes.models.SuccessResponse;
import io.km.notes.models.ErrorResponse;
import io.km.notes.services.NotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);

    private final NotesService notesService;

    @Autowired
    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    /**
     * Retrieves all notes and returns them in a SuccessResponse.
     * Logs the entry into the method and the status of the operation.
     *
     * @return ResponseEntity containing SuccessResponse with notes and HTTP status
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<List<Note>>> getAllNotes() {
        logger.info("Received request to get all notes.");
        SuccessResponse<List<Note>> response = notesService.getAllNotes();
        logger.info("Returning response with {} notes.", response.getData().size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Saves a new note. Determines the response based on the success or failure of the operation.
     * Logs the entry into the method, request details, and response status.
     *
     * @param note Note object to be saved
     * @return ResponseEntity containing SuccessResponse or ErrorResponse and appropriate HTTP status
     */
    @PostMapping
    public ResponseEntity<Object> saveNote(@RequestBody Note note) {
        logger.info("Received request to save note: {}", note);
        Object response = notesService.saveNote(note);

        // Handle response and logging based on response type
        if (response instanceof SuccessResponse<?> successResponse) {

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } else if (response instanceof ErrorResponse errorResponse) {
            logger.warn("Failed to save note: {}", errorResponse.getErrors().getFirst().getDetail());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            logger.error("Unexpected response type: {}", response.getClass().getName());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a specific note by its ID. Logs the request and response status.
     *
     * @param id The ID of the note to retrieve
     * @return ResponseEntity containing SuccessResponse with the note or ErrorResponse and appropriate HTTP status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getANote(@PathVariable String id) {
        logger.info("Received request to get note with ID: {}", id);
        Object response = notesService.getANote(id);

        if (response instanceof SuccessResponse) {

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response instanceof ErrorResponse) {
            logger.warn("No note found for ID: {}", id);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            logger.error("Unexpected response type: {}", response.getClass().getName());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing note. Determines the response based on the success or failure of the operation.
     * Logs the entry into the method, request details, and response status.
     *
     * @param note Note object with updated details
     * @return ResponseEntity containing SuccessResponse or ErrorResponse and appropriate HTTP status
     */
    @PutMapping
    public ResponseEntity<Object> updateNote(@RequestBody Note note) {
        logger.info("Received request to update note: {}", note);
        Object response = notesService.updateNote(note);

        if (response instanceof SuccessResponse<?> successResponse) {

            return new ResponseEntity<>(response, HttpStatus.OK);

        } else if (response instanceof ErrorResponse errorResponse) {
            logger.error("Failed to update note: {}", errorResponse.getErrors().getFirst().getDetail());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            logger.error("Unexpected response type: {}", response.getClass().getName());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a note by its ID. Determines the response based on the success or failure of the operation.
     * Logs the entry into the method, request details, and response status.
     *
     * @param id The ID of the note to delete
     * @return ResponseEntity containing SuccessResponse or ErrorResponse and appropriate HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable String id) {
        logger.info("Received request to delete note with ID: {}", id);
        Object response = notesService.deleteNote(id);

        if (response instanceof SuccessResponse) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response instanceof ErrorResponse errorResponse) {
            logger.error("Failed to delete note with ID: {}. Error: {}", id, errorResponse.getErrors().getFirst().getDetail());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            logger.error("Unexpected response type: {}", response.getClass().getName());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
