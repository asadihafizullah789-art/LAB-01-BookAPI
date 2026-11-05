package com.example.demo.model;

public record Book(
        Long id,
        String title,
        String author,
        int availableCopies
) {}