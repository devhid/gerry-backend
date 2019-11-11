package edu.stonybrook.cse308.gerrybackend.data;

import edu.stonybrook.cse308.gerrybackend.enums.converters.ElectionTypeConverter;
import edu.stonybrook.cse308.gerrybackend.enums.converters.PoliticalPartyConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity(name="election")
public class ElectionData {

    @Getter
    @Id
    @Column(name="id")
    private String id;

    @Getter
    @NotNull
    @Convert(converter = ElectionTypeConverter.class)
    @Column(name="election_id")
    private ElectionType electionId;

    @NotNull
    @ElementCollection
    @Column(name="votes")
    private Map<PoliticalParty, Integer> votes;

    @Getter
    @NotNull
    @Convert(converter = PoliticalPartyConverter.class)
    @Column(name="winner")
    private PoliticalParty winner;

    public ElectionData(){
        this.id = UUID.randomUUID().toString();
        this.electionId = ElectionType.getDefault();
        this.votes = new EnumMap<>(PoliticalParty.class);
        this.winner = PoliticalParty.getDefault();
        MapUtils.initMap(this.votes, 0);
    }

    public ElectionData(UUID id, ElectionType electionId, Map<PoliticalParty, Integer> votes, PoliticalParty winner){
        this.id = id.toString();
        this.electionId = electionId;
        this.votes = votes;
        this.winner = winner;
    }

    public int getVotesByParty(PoliticalParty party){
        if (party == PoliticalParty.NOT_SET){
            throw new IllegalArgumentException("Replace this string later!");
        }
        return this.votes.get(party);
    }

    public int getTotalVotes(){
        Collection<Integer> allVotes = this.votes.values();
        return allVotes.stream().mapToInt(Integer::intValue).sum();
    }

    public Map<PoliticalParty, Integer> getVotesCopy() {
        return new EnumMap<>(this.votes);
    }

    public static ElectionData[] combineElectionDataArr(ElectionData[] e1, ElectionData[] e2)
            throws MismatchedElectionException {
        ElectionType[] electionTypes = ElectionType.values();
        ElectionData[] newElectionDataArr = new ElectionData[electionTypes.length];
        for (ElectionType electionId : electionTypes){
            int index = electionId.value;
            newElectionDataArr[index] = ElectionData.combine(e1[index], e2[index]);
        }
        return newElectionDataArr;
    }

    public static Map<ElectionType, ElectionData> combineElectionDataMaps(Map<ElectionType, ElectionData> e1,
                                                                          Map<ElectionType, ElectionData> e2)
            throws MismatchedElectionException {
        Set<ElectionType> electionIds = e1.keySet();
        if (!electionIds.equals(e2.keySet())){
            throw new IllegalArgumentException("Replace this string later!");
        }
        Map<ElectionType, ElectionData> combinedMap = new EnumMap<>(ElectionType.class);
        for (ElectionType electionId : electionIds){
            ElectionData e1Data = e1.get(electionId);
            ElectionData e2Data = e2.get(electionId);
            // Check if either map explicitly mapped this to null.
            if ((e1Data == null) || (e2Data == null)){
                throw new IllegalArgumentException("Replace this string later!");
            }
            combinedMap.put(electionId, ElectionData.combine(e1.get(electionId), e2.get(electionId)));
        }
        return combinedMap;
    }

    public static ElectionData combine(ElectionData e1, ElectionData e2) throws MismatchedElectionException {
        if (e1.electionId != e2.electionId){
            throw new MismatchedElectionException("Replace this string later.");
        }

        Map<PoliticalParty, Integer> e1Votes = e1.getVotesCopy();
        Map<PoliticalParty, Integer> e2Votes = e2.getVotesCopy();
        Set<PoliticalParty> partyTypes = e1Votes.keySet();
        if (!partyTypes.equals(e2Votes.keySet())){
            throw new IllegalArgumentException("Replace this string later!");
        }

        Map<PoliticalParty, Integer> combinedVotes = new EnumMap<>(PoliticalParty.class);
        for (PoliticalParty partyType : partyTypes){
            Integer e1NumVotes = e1Votes.get(partyType);
            Integer e2NumVotes = e2Votes.get(partyType);
            // Check if either map explicitly mapped this to null.
            if ((e1NumVotes == null) || (e2NumVotes == null)){
                throw new IllegalArgumentException("Replace this string later!");
            }
            combinedVotes.put(partyType, e1Votes.get(partyType) + e2Votes.get(partyType));
        }
        int max = combinedVotes.values().stream().mapToInt(Integer::intValue).max().getAsInt();
        long maxOccurrences = combinedVotes.entrySet().stream().filter(
                entry -> entry.getValue() == max).count();
        PoliticalParty winner = (maxOccurrences > 1) ? PoliticalParty.TIE : PoliticalParty.getDefault();
        if (maxOccurrences == 1){
            winner = combinedVotes.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(Map.Entry::getKey)
                    .findFirst().get();
        }
        return new ElectionData(UUID.randomUUID(), e1.electionId, combinedVotes, winner);
    }
}
