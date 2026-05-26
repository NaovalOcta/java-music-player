package test.function.validator;

import java.io.File;

import function.validator.AudioFormatValidator;

public class StubInvalidAudioFormatValidator implements AudioFormatValidator {
    @Override
    public boolean isSupported(File file) {
        // STUB: Selalu mengembalikan nilai FALSE
        return false; 
    }
}
