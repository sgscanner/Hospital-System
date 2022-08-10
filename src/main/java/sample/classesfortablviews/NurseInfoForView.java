package sample.classesfortablviews;

public class NurseInfoForView {
    String id, name, depart;

    public NurseInfoForView(String id, String name, String depart) {
        this.id = id;
        this.name = name;
        this.depart = depart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }
}
