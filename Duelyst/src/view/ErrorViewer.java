package view;

public class ErrorViewer {
    private static ErrorViewer singleInstance = null;

    public static ErrorViewer getInstance() {
        if (singleInstance == null) {
            singleInstance = new ErrorViewer();
        }
        return singleInstance;
    }

    public void collectionError(String error){
        System.out.println(error);
    }
}