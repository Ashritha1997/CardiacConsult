package com.project.cardiacconsult.models;

public class Feedback {


    private float Overallrating;

    private float accuracy;

    private float diagnosis;

    private float phyrating;

    public Feedback(){

    }

    public Feedback(float overallrating, float accuracy, float diagnosis, float phyrating)
    {
        Overallrating=overallrating;
        this.accuracy=accuracy;
        this.diagnosis=diagnosis;
        this.phyrating=phyrating;

    }

    public void setOverallrating(float overallrating) {
        Overallrating = overallrating;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public void setDiagnosis(float diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setPhyrating(float phyrating) {
        this.phyrating = phyrating;
    }

    public float getOverallrating() {
        return Overallrating;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public float getDiagnosis() {
        return diagnosis;
    }

    public float getPhyrating() {
        return phyrating;
    }
}
