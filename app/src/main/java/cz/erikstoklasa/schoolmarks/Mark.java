package cz.erikstoklasa.schoolmarks;

public class Mark {
    private int mark;
    private String date;
    private String markDescription;

    public Mark(int mark, String date, String markDescription) {
        this.mark = mark;
        this.date = date;
        this.markDescription = markDescription;
    }
    public float getMark() {
        return (float) mark;
    }

    public String getDate() {
        return date;
    }

    public String getMarkDescription() {
        return markDescription;
    }
}
