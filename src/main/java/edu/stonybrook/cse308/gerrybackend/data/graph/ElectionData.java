package edu.stonybrook.cse308.gerrybackend.data.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stonybrook.cse308.gerrybackend.enums.converters.ElectionTypeConverter;
import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;
import edu.stonybrook.cse308.gerrybackend.exceptions.MismatchedElectionException;
import edu.stonybrook.cse308.gerrybackend.utils.MapUtils;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Embeddable
@JsonIgnoreProperties({"votesCopy"})
public class ElectionData {

    @Getter
    @NotNull
    @Convert(converter = ElectionTypeConverter.class)
    @Column(name = "election_type")
    private ElectionType electionType;

    @NotNull
    @BatchSize(size = 500)  // IMPORTANT
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "votes")
    @JsonProperty("votes")
    private Map<PoliticalParty, Integer> votes;

    @Getter
    @NotNull
    @BatchSize(size = 500)  // IMPORTANT
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<PoliticalParty> winners;

    public ElectionData() {
        this.electionType = ElectionType.getDefault();
        this.votes = new EnumMap<>(PoliticalParty.class);
        this.winners = new HashSet<>();
        MapUtils.initMap(this.votes, 0);
    }

    public ElectionData(ElectionType electionType, Map<PoliticalParty, Integer> votes, Set<PoliticalParty> winners) {
        this.electionType = electionType;
        this.votes = votes;
        this.winners = winners;
    }

    public ElectionData(ElectionData obj) {
        this(obj.electionType, obj.getVotesCopy(), obj.getWinnersCopy());
    }

    public int getPartyVotes(PoliticalParty party) {
        return this.votes.getOrDefault(party, 0);
    }

    public int getAllOtherPartyVotes(PoliticalParty party) {
        int allOtherVotes = 0;
        for (Map.Entry<PoliticalParty, Integer> entry : this.votes.entrySet()) {
            if (entry.getKey() != party) {
                allOtherVotes += entry.getValue();
            }
        }
        return allOtherVotes;
    }

    public void setPartyVotes(PoliticalParty party, int partyVotes) {
        this.votes.put(party, partyVotes);
    }

    public int getTotalVotes() {
        return MapUtils.sumIntValuedMap(this.votes);
    }

    public Map<PoliticalParty, Integer> getVotesCopy() {
        return new EnumMap<>(this.votes);
    }

    public Set<PoliticalParty> getWinnersCopy() {
        return new HashSet<>(this.winners);
    }

    private static Set<PoliticalParty> determineWinners(Map<PoliticalParty, Integer> votes) {
        final int max = votes.values().stream().mapToInt(Integer::intValue).max().getAsInt();
        final Set<PoliticalParty> winners = new HashSet<>();
        votes.forEach((party, numVotes) -> {
            if (numVotes == max) {
                winners.add(party);
            }
        });
        return winners;
    }

    public static ElectionData combine(ElectionData e1, ElectionData e2) throws MismatchedElectionException {
        ElectionData combined = new ElectionData(e1.getElectionType(), e1.getVotesCopy(), e1.getWinnersCopy());
        combined.add(e2);
        return combined;
    }

    public void add(ElectionData e) throws MismatchedElectionException {
        if (e.electionType != this.electionType) {
            throw new MismatchedElectionException("Replace this string later.");
        }

        Map<PoliticalParty, Integer> eVotes = e.votes;
        Set<PoliticalParty> partyTypes = eVotes.keySet();

        for (PoliticalParty partyType : partyTypes) {
            Integer e1NumVotes = eVotes.getOrDefault(partyType, 0);
            Integer e2NumVotes = this.votes.getOrDefault(partyType, 0);
            // Check if either map explicitly mapped this to null.
            if ((e1NumVotes == null) || (e2NumVotes == null)) {
                throw new IllegalArgumentException("Replace this string later!");
            }
            this.votes.put(partyType, e1NumVotes + e2NumVotes);
        }
        this.winners = ElectionData.determineWinners(this.votes);
    }

    public void subtract(ElectionData subElectionData) throws MismatchedElectionException {
        if (this.electionType != subElectionData.electionType) {
            throw new MismatchedElectionException("Replace this string later.");
        }

        Map<PoliticalParty, Integer> subVotes = subElectionData.votes;

        for (PoliticalParty partyType : this.votes.keySet()) {
            int bigPartyVotes = this.votes.getOrDefault(partyType, 0);
            int smallPartyVotes = subVotes.getOrDefault(partyType, 0);
            if (bigPartyVotes < smallPartyVotes) {
                throw new IllegalArgumentException("Replace this string later!");
            }
            this.votes.put(partyType, bigPartyVotes - smallPartyVotes);
        }
        this.winners = ElectionData.determineWinners(this.votes);
    }
}
