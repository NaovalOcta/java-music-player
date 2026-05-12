package function.validator;

import java.io.File;

public interface AudioFormatValidator {
  boolean isSupported(File file);
}
