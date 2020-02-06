package lecture.mobile.final_project.ma02_20151019;

public class CalData {
    int day;
    int dayofweek;

    public CalData(int d, int h) {
        day = d;
        dayofweek = h;
    }

    public int getDay() {
        return day;
    }

    public int getDayofweek() {
        return dayofweek;
    }
}