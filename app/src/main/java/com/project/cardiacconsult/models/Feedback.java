package com.project.cardiacconsult.models;

public class Feedback
{

    private float overallrating;

    private float accuracy;

    private float Diagnosis;

    private float Phyrating;


     public  Feedback(){}

     public Feedback(float overallrating, float accuracy, float Diagnosis, float Phyrating)
     {

        this.overallrating=overallrating;
        this.accuracy=accuracy;
        this.Diagnosis=Diagnosis;
        this.Phyrating=Phyrating;

     }


    public float getAccuracy() {
        return accuracy;
    }

    public float getOverallrating() {
        return overallrating;
    }

    public float getDiagnosis() {
        return Diagnosis;
    }

    public float getPhyrating() {
        return Phyrating;
    }

    public void setOverallrating(float overallrating) {
        this.overallrating = overallrating;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public void setDiagnosis(float diagnosis) {
        Diagnosis = diagnosis;
    }

    public void setPhyrating(float phyrating) {
        Phyrating = phyrating;
    }


   /* @Override
    public String toString() {
        return "Feedback{" +
                "Overallrating='" + overallrating + '\'' +
                ", Accuracy='" + accuracy + '\'' +
                ", Diagnosis='" + Diagnosis + '\'' +
                ", Phyrating='" + Phyrating + '\'' +
                '}';
    }*/
}
