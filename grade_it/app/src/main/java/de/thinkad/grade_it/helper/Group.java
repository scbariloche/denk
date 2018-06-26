package de.thinkad.grade_it.helper;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public ArrayList<Object> getObjects() {
        return objects;
    }

    ArrayList<Object> objects = new ArrayList();
    String name;

    public Group(String name, int groupWeight, List<Object> objects) {
        this.name = name;
        this.groupWeight = groupWeight;
        this.addAll(objects);
    }

    public Group() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int groupWeight;

    public Object get(int index) {
        return this.objects.get(index);
    }

    public int getGroupWeight() {
        return groupWeight;
    }

    public void setGroupWeight(int groupWeight) {
        this.groupWeight = groupWeight;
    }

    public void add(Object obj) {
        this.objects.add(obj);
    }

    public void addAll(List<Object> obj) {
        this.objects.addAll(obj);
    }

    public int size() {
        return this.objects.size();
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        ret.append(getName());

        return ret.toString();
    }

    static int ebene;

    public void buildExplanationString(StringBuilder builder) {

        for (Object i : objects) {
            for (int padding = 0; padding < ebene; padding++) {
                builder.append("\t");
            }
            SubjectCategory subjectCategory = new SubjectCategory();
            Group group;
            if (i instanceof SubjectCategory) {
                subjectCategory = (SubjectCategory) i;
                builder.append("hier z�hlt ein(e) " + subjectCategory.getCateID() + " " + subjectCategory.getWeight()
                        + "-fach" + "\n");

            } else if (i instanceof Group) {
                ebene++;
                group = (Group) i;
                if (ebene > 1) {
                    builder.append("hier z�hlt der Durchschnitt aus allen " + group.getName() + " " + group.getGroupWeight() + "-fach" + "\n");
                } else {
                    builder.append("der Durchschnitt aus allen " + group.getName() + " z�hlt " + group.getGroupWeight() + "-fach" + "\n");
                }
                group.buildExplanationString(builder);
                ebene--;
            }
        }

    }

    public float calculateAvarage() {
        int weightsum = 0;
        float sum1 = 0f;
        for (Object i : objects) {
            for (int padding = 0; padding < ebene; padding++) {
            }
            SubjectCategory subjectCategory = new SubjectCategory();
            Group group = new Group();
            if (i instanceof SubjectCategory) {
                subjectCategory = (SubjectCategory) i;
                weightsum += subjectCategory.getWeight();
                float sum2 = 0f;
                for (int x = 0; x < subjectCategory.getGrades().size(); x++) {
                    sum2 += subjectCategory.getGrades().get(x);
                }
                sum1 += (sum2 / subjectCategory.getGrades().size()) * subjectCategory.getWeight();

            } else if (i instanceof Group) {
                ebene++;
                group = (Group) i;
                weightsum += group.getGroupWeight();
                sum1 += group.calculateAvarage() * group.getGroupWeight();
                ebene--;
            }
        }
        return sum1 / weightsum;
    }
}
