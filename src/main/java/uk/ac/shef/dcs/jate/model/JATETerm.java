package uk.ac.shef.dcs.jate.model;

/**
 * Created by zqz on 19/09/2015.
 */
public class JATETerm implements Comparable<JATETerm>{

    protected String string;
    protected double score;
    protected TermInfo termInfo;

    public JATETerm(String string){
        this(string, 0.0);
    }
    public JATETerm(String string, double score){
        this.string=string;
        this.score=score;
    }

    public String getString(){
        return string;
    }

    public void setString(String string){
        this.string=string;
    }

    public double getScore(){
        return score;
    }

    public void setScore(double score){
        this.score=score;
    }

    public TermInfo getTermInfo(){
        return termInfo;
    }

    public void setTermInfo(TermInfo info){
        this.termInfo= info;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder(string);
        sb.append("=").append(score);
        return sb.toString();
    }


    @Override
    public int compareTo(JATETerm o) {
        int result= Double.valueOf(o.getScore()).compareTo(getScore());
        if (result==0)
            return o.getString().compareTo(getString());
        return result;
    }
}
