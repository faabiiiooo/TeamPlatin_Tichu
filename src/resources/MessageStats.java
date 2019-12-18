package resources;

//@author Fabio
public enum MessageStats {

    inEvaluation, accepted, declined;

    public String toString(){

        String status = null;

        switch (this){
            case accepted: status = "accepted"; break;
            case declined: status = "declined"; break;
            case inEvaluation: status = "inEvaluation"; break;

        }

        return status;
    }

}
