package test.function.validator;

import java.io.File;

import function.validator.AudioFormatValidator;

public class StubValidAudioFormatValidator implements AudioFormatValidator {
    @Override
    public boolean isSupported(File file) {
        // STUB: Selalu mengembalikan nilai TRUE tanpa peduli ekstensi file-nya apa
        return true; 
    }
}
