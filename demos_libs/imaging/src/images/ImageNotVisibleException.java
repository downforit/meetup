package mlpks.tblimaging2.images;



public class ImageNotVisibleException extends RuntimeException {
  public ImageNotVisibleException() {
    super();
  }

  public ImageNotVisibleException(String message) {
    super(message);
  }

  public ImageNotVisibleException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImageNotVisibleException(Throwable cause) {
    super(cause);
  }
}
