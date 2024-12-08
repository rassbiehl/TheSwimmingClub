package swimclub.services;

import swimclub.models.ActivityType;
import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.models.TrainingResults;
import swimclub.repositories.TrainingResultsRepository;


import java.util.List;

public class TrainingResultsService {
    private final TrainingResultsRepository resultsRepository;

    public TrainingResultsService(TrainingResultsRepository resultsRepository){
        this.resultsRepository = resultsRepository;
    }
    public void addResult(Member member, ActivityType activityType, double time, String date, MembershipLevel level){
        if (activityType == null){
            throw new IllegalArgumentException("Fill in disciplne.");
        }
        if (time == -1){
            throw new IllegalArgumentException("Time must be positive");
        }
        if(date == null){
            throw new IllegalArgumentException("Fill in date.");
        }
        TrainingResults results = new TrainingResults(member,level, activityType, time, date);
        resultsRepository.addResults(results);
    }

    public List<TrainingResults> getResultsByMember(Member member){
        if (member == null){
            throw new IllegalArgumentException("Member must not be empty");
        }
        return resultsRepository.getResultsByMember(member);
    }

    public List<TrainingResults> getAllResults(){
        return resultsRepository.getAllResults();
    }
}
