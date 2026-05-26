package gui;

import function.validator.AudioFormatValidator;
import test.function.validator.StubValidAudioFormatValidator;
import test.function.validator.StubInvalidAudioFormatValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JFrame;

public class MainGUIPanelTest {

    @Test
    public void testIsValidSongFolder_WithStubValid_ReturnTrue() {
        AudioFormatValidator stubValid = new StubValidAudioFormatValidator();
        JFrame dummyFrame = new JFrame();

        MainGUIPanel panel = new MainGUIPanel(dummyFrame, stubValid);

        panel.getAbsoluteSongPath = ".";

        boolean result = panel.isValidSongFolder();

        assertTrue(result, "isValidSongFolder() harus mengembalikan true karena didukung oleh StubValid");
    }

    @Test
    public void testIsValidSongFolder_WithStubInvalid_ReturnFalse() {
        AudioFormatValidator stubInvalid = new StubInvalidAudioFormatValidator();
        JFrame dummyFrame = new JFrame();

        MainGUIPanel panel = new MainGUIPanel(dummyFrame, stubInvalid);
        panel.getAbsoluteSongPath = ".";

        boolean result = panel.isValidSongFolder();

        assertFalse(result, "isValidSongFolder() harus mengembalikan false karena ditolak oleh StubInvalid");
    }
}