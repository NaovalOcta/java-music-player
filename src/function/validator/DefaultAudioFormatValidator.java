package function.validator;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DefaultAudioFormatValidator implements AudioFormatValidator {
  private static final Set<String> SUPPORTED_AUDIO_FORMATS = new HashSet<>(
      Arrays.asList(".mp3", ".wav", ".flac"));

  @Override
  public boolean isSupported(File file) {
    if (file == null || !file.isFile()) {
      return false;
    }

    String fileName = file.getName().toLowerCase(Locale.ROOT);
    for (String extension : SUPPORTED_AUDIO_FORMATS) {
      if (fileName.endsWith(extension)) {
        return true;
      }
    }

    return false;
  }
}
