package com.example.hoangvancook.Listeners;

import com.example.hoangvancook.Models.InstructionsResponse;

import java.util.List;

public interface InstructionsListener {
    void didFetch(List<InstructionsResponse> response, String message);
    void didError(String message);
}
