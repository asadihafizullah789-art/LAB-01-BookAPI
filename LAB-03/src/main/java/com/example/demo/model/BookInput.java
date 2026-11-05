package com.example.demo.model;

public record BookInput(
        String title,
        String author,
        int availableCopies
) {}