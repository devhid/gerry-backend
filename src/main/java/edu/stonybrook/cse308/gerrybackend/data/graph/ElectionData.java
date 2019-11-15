package edu.stonybrook.cse308.gerrybackend.data.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties({"votesCopy"})
public class ElectionData {

    @Getter
    @Id
    @Column(name="id")
    private String id;

    @Getter
    @NotNull
    @Convert(converter=ElectionTypeConverter.class)
    @Column(name="election_type")
    private ElectionType electionType;

    @NotNull
    @ElementCollection
    @Column(name="votes")
    @JsonProperty("votes")
    private Map<PoliticalParty, Integer> votes;

    @Getter
    @NotNull
    @Convert(converter=PoliticalPartyConverter.class)
    @Column(name="winner")
    private PoliticalParty winner;

    public ElectionData(){
        this.id = UUID.randomUUID().toString();
        this.electionType = ElectionType.getDefault();
        this.votes = new EnumMap<>(PoliticalParty.class);
        this.winner = PoliticalParty.getDefault();
        MapUtils.initMap(this.votes, 0);
    }

    public ElectionData(String id, ElectionType electionType, Map<PoliticalParty, Integer> votes, PoliticalParty winner){
        this.id = id.toString();
        this.electionType = electionType;
        this.votes = votes;
        this.winner = winner;
    }

    public int getPartyVotes(PoliticalParty party){
        if (party == PoliticalParty.NOT_SET){
            throw new IllegalArgumentException("Replace this string later!");
        }
        return this.votes.get(party);
    }

    public void setPartyVotes(PoliticalParty party, int partyVotes){
        this.votes.put(party, partyVotes);
    }

    public int getTotalVotes(){
        Collection<Integer> allVotes = this.votes.values();
        return allVotes.stream().mapToInt(Integer::intValue).sum();
    }

    public Map<PoliticalParty, Integer> getVotesCopy() {
        return new EnumMap<>(this.votes);
    }

    public static ElectionData combine(ElectionData e1, ElectionData e2) throws MismatchedElectionException {
        if (e1.electionType != e2.electionType){
            throw new MismatchedElectionException("Replace this string later.");
        }

        Map<PoliticalParty, Integer> e1Votes = e1.votes;
        Map<PoliticalParty, Integer> e2Votes = e2.votes;
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
        return new ElectionData(UUID.randomUUID().toString(), e1.electionType, combinedVotes, winner);
    }
}
