package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.BookInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    // We use ArrayList instead of List.of() so we can modify (update/delete) it
    private final List<Book> books = new ArrayList<>(List.of(
            new Book(1L, "Java Programming", "John Smith", 5),
            new Book(2L, "Web Development", "Sara Ahmad", 3),
            new Book(3L, "Database Systems", "Ali Khan", 4)
    ));

    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(
            @PathVariable Long bookId,
            @RequestBody BookInput input) {

        // 1. Search for the book using bookId
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).id().equals(bookId)) {

                // 2. Create the updated Book object (keep original ID)
                Book updatedBook = new Book(
                        bookId,
                        input.title(),
                        input.author(),
                        input.availableCopies()
                );

                // 3. Replace the old book in the list
                books.set(i, updatedBook);

                // 4. Return the updated book with HTTP 200
                return ResponseEntity.ok(updatedBook);
            }
        }

        // 5. Return 404 if the book is not found
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {

        // 1 & 2. Search for the book and remove it from the list
        boolean removed = books.removeIf(book -> book.id().equals(bookId));

        // 3. Return 204 if successfully deleted
        if (removed) {
            return ResponseEntity.noContent().build();
        }

        // 4. Return 404 if the book does not exist
        return ResponseEntity.notFound().build();
    }
}