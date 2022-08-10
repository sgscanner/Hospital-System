package sample.classesfortablviews;
public class DawamDoc {
    String dayName, timeOfDawam;

    public DawamDoc(String dayName, String timeOfDawam) {
        this.dayName = dayName;
        this.timeOfDawam = timeOfDawam;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getTimeOfDawam() {
        return timeOfDawam;
    }

    public void setTimeOfDawam(String timeOfDawam) {
        this.timeOfDawam = timeOfDawam;
    }
}